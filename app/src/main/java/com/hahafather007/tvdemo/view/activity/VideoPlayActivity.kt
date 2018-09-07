package com.hahafather007.tvdemo.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.*
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_play)

        hideBottomBtn()
    }

    /**
     * 自定义返回键
     */
    override fun onBackPressed() {}

    /**
     * 隐藏底部虚拟按键
     */
    private fun hideBottomBtn() {
        val decorView = window.decorView
        val uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION or SYSTEM_UI_FLAG_IMMERSIVE_STICKY or SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }
}
