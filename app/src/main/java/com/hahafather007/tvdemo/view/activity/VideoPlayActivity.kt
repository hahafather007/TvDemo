package com.hahafather007.tvdemo.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View.*
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.annimon.stream.function.Supplier
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.common.DataBindingItemViewBinder
import com.hahafather007.tvdemo.common.RxController
import com.hahafather007.tvdemo.databinding.ActivityVideoPlayBinding
import com.hahafather007.tvdemo.databinding.ItemTvTitleBinding
import com.hahafather007.tvdemo.model.data.TvData
import com.hahafather007.tvdemo.utils.RxField
import com.hahafather007.tvdemo.utils.disposable
import com.hahafather007.tvdemo.utils.log
import com.hahafather007.tvdemo.view.fragment.VideoPlayFragment
import com.hahafather007.tvdemo.viewmodel.VideoPlayViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class VideoPlayActivity : AppCompatActivity(),
        DataBindingItemViewBinder.OnBindItem<Any, ViewDataBinding>, RxController {
    override val rxComposite = CompositeDisposable()

    private lateinit var binding: ActivityVideoPlayBinding
    private val homeBtnReceiver = HomeBtnReceiver()
    private val viewModel = VideoPlayViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_play)
        binding.activity = this
        binding.viewModel = viewModel

        hideBottomBtn()
        initReceiver()
        addChangeListener()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(homeBtnReceiver)
        onCleared()
        viewModel.onCleared()
    }

    /**
     * 自定义返回键
     */
    override fun onBackPressed() {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
        // 确定键
            KEYCODE_ENTER, KEYCODE_DPAD_CENTER -> openOrCloseDrawer()
        // 返回键
            KEYCODE_BACK -> "back".log()
        // 设置键
            KEYCODE_SETTINGS -> "setting".log()
        // 上/下/左/右键
            KEYCODE_DPAD_DOWN -> {
                "↓".log()

                viewModel.nextTv()
            }
            KEYCODE_DPAD_UP -> {
                "↑".log()
                viewModel.lastTv()
            }
            KEYCODE_DPAD_LEFT -> "←".log()
            KEYCODE_DPAD_RIGHT -> "→".log()
        // 数字键0~9
            KEYCODE_0 -> "0".log()
            KEYCODE_1 -> "1".log()
            KEYCODE_2 -> "2".log()
            KEYCODE_3 -> "3".log()
            KEYCODE_4 -> "4".log()
            KEYCODE_5 -> "5".log()
            KEYCODE_6 -> "6".log()
            KEYCODE_7 -> "7".log()
            KEYCODE_8 -> "8".log()
            KEYCODE_9 -> "9".log()
        // 后一个视频
            KEYCODE_PAGE_DOWN, KEYCODE_MEDIA_NEXT -> "下一曲".log()
        // 前一个视频
            KEYCODE_PAGE_UP, KEYCODE_MEDIA_PREVIOUS -> "上一曲".log()
        // 音量+
            KEYCODE_VOLUME_UP -> "音量+".log()
        // 音量-
            KEYCODE_VOLUME_DOWN -> "音量-".log()
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun bind(dataBinding: ViewDataBinding, data: Any, position: Int) {
        if (dataBinding is ItemTvTitleBinding) {
            dataBinding.data = data as TvData
            dataBinding.viewModel = viewModel
        }
    }

    /**
     * 隐藏底部虚拟按键
     */
    private fun hideBottomBtn() {
        val decorView = window.decorView
        val uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    /**
     * 注册广播
     */
    private fun initReceiver() {
        val filter = IntentFilter(ACTION_CLOSE_SYSTEM_DIALOGS)
        registerReceiver(homeBtnReceiver, filter)
    }

    private fun addChangeListener() {
        RxField.of(viewModel.currentTv)
                .disposable(this)
                .doOnNext {
                    val fm = VideoPlayFragment.getFragmentByUrl(it.url)
                    val transaction = supportFragmentManager.beginTransaction()

                    transaction.replace(R.id.fragment, fm)
                            .commit()
                }
                .subscribe()
    }

    /**
     * 打开或者关闭抽屉
     */
    fun openOrCloseDrawer() {
        val moveValue = if (binding.drawer.x < 0) binding.drawer.width else 0

        moveValue.log()

        binding.drawer.animate()
                .translationX(moveValue.toFloat())
                .setDuration(500)
                .start()
    }

    /**
     * 监听Home键的广播
     */
    class HomeBtnReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_CLOSE_SYSTEM_DIALOGS &&
                    SYSTEM_DIALOG_REASON_HOME_KEY == intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)) {
                "Home".log()
            }
        }
    }

    companion object {
        private const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        private const val SYSTEM_DIALOG_REASON_KEY = "reason"
    }
}
