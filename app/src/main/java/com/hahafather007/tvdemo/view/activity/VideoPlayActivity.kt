package com.hahafather007.tvdemo.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View.*
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.common.DataBindingItemViewBinder
import com.hahafather007.tvdemo.common.RxController
import com.hahafather007.tvdemo.databinding.ActivityVideoPlayBinding
import com.hahafather007.tvdemo.databinding.ItemTvTitleBinding
import com.hahafather007.tvdemo.model.data.TvData
import com.hahafather007.tvdemo.utils.*
import com.hahafather007.tvdemo.view.fragment.VideoPlayFragment
import com.hahafather007.tvdemo.viewmodel.VideoPlayViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import jp.wasabeef.blurry.internal.Blur
import jp.wasabeef.blurry.internal.BlurFactor
import java.util.concurrent.TimeUnit

class VideoPlayActivity : AppCompatActivity(),
        DataBindingItemViewBinder.OnBindItem<Any, ViewDataBinding>, RxController {
    override val rxComposite = CompositeDisposable()

    private lateinit var binding: ActivityVideoPlayBinding
    private val homeBtnReceiver = HomeBtnReceiver()
    private val viewModel = VideoPlayViewModel()
    private var videoFragment: VideoPlayFragment? = null
    /**
     * UI是否绘制完毕
     */
    private var isUiFinished = false
    /**
     * 抽屉是否打开
     */
    private var isDrawerOpen = false
    /**
     * 截图剪切的属性
     */
    private var realY = 0
    private var realHeight = 0
    private var realWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_play)
        binding.activity = this
        binding.viewModel = viewModel

        hideBottomBtn()
        initReceiver()
        addChangeListener()
        initReals()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        isUiFinished = hasFocus
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
            KEYCODE_BACK -> {
                "back".log()

                if (isDrawerOpen) {
                    isDrawerOpen = false

                    binding.drawer.animate()
                            .translationX(0f)
                            .setDuration(200)
                            .start()
                }
            }
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
            KEYCODE_DPAD_LEFT -> {
                "←".log()

                controlVolume(false)
            }
            KEYCODE_DPAD_RIGHT -> {
                "→".log()

                controlVolume(true)
            }
        // 数字键0~9
            KEYCODE_0 -> {
                "0".log()

                viewModel.setTvIndex(0)
            }
            KEYCODE_1 -> {
                "1".log()

                viewModel.setTvIndex(1)
            }
            KEYCODE_2 -> {
                "2".log()

                viewModel.setTvIndex(2)
            }
            KEYCODE_3 -> {
                "3".log()

                viewModel.setTvIndex(3)
            }
            KEYCODE_4 -> {
                "4".log()

                viewModel.setTvIndex(4)
            }
            KEYCODE_5 -> {
                "5".log()

                viewModel.setTvIndex(5)
            }
            KEYCODE_6 -> {
                "6".log()

                viewModel.setTvIndex(6)
            }
            KEYCODE_7 -> {
                "7".log()

                viewModel.setTvIndex(7)
            }
            KEYCODE_8 -> {
                "8".log()

                viewModel.setTvIndex(8)
            }
            KEYCODE_9 -> {
                "9".log()

                viewModel.setTvIndex(9)
            }
        // 后一个视频
            KEYCODE_PAGE_DOWN, KEYCODE_MEDIA_NEXT -> {
                "下一曲".log()

                viewModel.nextTv()
            }
        // 前一个视频
            KEYCODE_PAGE_UP, KEYCODE_MEDIA_PREVIOUS -> {
                "上一曲".log()

                viewModel.lastTv()
            }
        // 音量+
            KEYCODE_VOLUME_UP -> {
                "音量+".log()

                controlVolume(true)
            }
        // 音量-
            KEYCODE_VOLUME_DOWN -> {
                "音量-".log()

                controlVolume(false)
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun bind(dataBinding: ViewDataBinding, data: Any, position: Int) {
        if (dataBinding is ItemTvTitleBinding) {
            dataBinding.index = viewModel.tvList.indexOf(data)
            dataBinding.data = data as TvData
            dataBinding.viewModel = viewModel
        }
    }

    /**
     * 控制音量
     * @param isAdd 是否为加音量
     */
    private fun controlVolume(isAdd: Boolean) {
        val fm = videoFragment

        if (fm != null) {
            if (isAdd) {
                viewModel.setVolume(fm.addVolume())
            } else {
                viewModel.setVolume(fm.subVolume())
            }
        }
    }

    private fun initReals() {
        val screenInfo = getScreenInfo()

        realY = (108 * 24 * screenInfo.density / screenInfo.height).toInt()
        realWidth = (192 * 180 * screenInfo.density / screenInfo.width).toInt()
        realHeight = 108 - realY

        realWidth.log()
    }

    private fun recyclerViewScroll() {
        binding.recyclerView.scrollToPosition(viewModel.tvList.indexOf(viewModel.currentTv.get()))
    }

    /**
     * 隐藏底部虚拟按键
     */
    private fun hideBottomBtn() {
        val decorView = window.decorView
        val uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_FULLSCREEN
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

                    videoFragment = fm
                    transaction.replace(R.id.fragment, fm)
                            .commit()

                    recyclerViewScroll()
                }
                .subscribe()

        // 计算高斯模糊的drawer背景
        Observable.interval(40, TimeUnit.MILLISECONDS)
                .filter {
                    isUiFinished && videoFragment != null && videoFragment!!.isPlayerValid()
                            && realHeight != 0 && realWidth != 0 && realY != 0 && isDrawerOpen
                }
                .flatMap {
                    Observable.just(videoFragment!!.getSnapshot())
                            .map { Bitmap.createBitmap(it, 0, realY, realWidth, realHeight) }
                }
                .flatMap {
                    val factor = BlurFactor()
                    factor.radius = 8
                    factor.width = it.width
                    factor.height = it.height
                    factor.color = Color.argb(0x66, 0x66, 0x66, 0x66)

                    Observable.just(Blur.of(this, it, factor))
                }
                .disposable(this)
                .asyncSwitch()
                .doOnNext {
                    binding.drawerGround.setImageBitmap(it)
                }
                .subscribe()
    }

    /**
     * 打开或者关闭抽屉
     */
    fun openOrCloseDrawer() {
        val moveValue = if (binding.drawer.x < -1) binding.drawer.width - 1 else 0

        isDrawerOpen = moveValue != 0

        binding.drawer.animate()
                .translationX(moveValue.toFloat())
                .setDuration(200)
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
