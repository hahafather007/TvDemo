package com.hahafather007.tvdemo.view.fragment

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer
import chuangyuan.ycj.videolibrary.video.ManualPlayer
import chuangyuan.ycj.videolibrary.video.MediaSourceBuilder
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.common.TestVideos
import com.hahafather007.tvdemo.databinding.FragmentVideoPlayBinding
import kotlinx.android.synthetic.main.fragment_video_play.view.*


class VideoPlayFragment : Fragment() {
    private lateinit var binding: FragmentVideoPlayBinding
    private lateinit var player: ExoUserPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!

        initVideo()
    }

    override fun onDestroy() {
        super.onDestroy()

        player.releasePlayers()
    }

    private fun initVideo() {
        player = VideoPlayerManager.Builder(VideoPlayerManager.TYPE_PLAY_USER, binding.videoView)
                .setPlayUri(Uri.parse(TestVideos.videos["香港卫视"]))
                .setDataSource { RtmpDataSourceFactory() }
                .create()
        player.startPlayer<ExoUserPlayer>()
        binding.videoView.videoView
    }
}