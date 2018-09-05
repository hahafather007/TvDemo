package com.hahafather007.tvdemo.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_play)
    }
}
