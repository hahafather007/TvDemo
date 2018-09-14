package com.hahafather007.tvdemo.view.fragment

import android.content.Intent.EXTRA_KEY_EVENT
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer
import chuangyuan.ycj.videolibrary.video.VideoPlayerManager
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.hahafather007.tvdemo.R
import com.hahafather007.tvdemo.common.RxController
import com.hahafather007.tvdemo.databinding.FragmentVideoPlayBinding
import com.hahafather007.tvdemo.model.pref.TvPref
import com.hahafather007.tvdemo.utils.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class VideoPlayFragment : Fragment(), RxController {
    override val rxComposite = CompositeDisposable()

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

    override fun onPause() {
        super.onPause()

        player.onPause()
    }

    override fun onResume() {
        super.onResume()

        player.onResume()
    }

    override fun onStop() {
        super.onStop()

        player.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        onCleared()
        player.releasePlayers()
    }

    private fun initVideo() {
        player = VideoPlayerManager.Builder(VideoPlayerManager.TYPE_PLAY_USER, binding.videoView)
                .setPlayUri(getUrl())
                .setPlayerGestureOnTouch(false)
                .setDataSource { RtmpDataSourceFactory() }
                .create()


        player.apply {
            startPlayer<ExoUserPlayer>()
            setShowVideoSwitch(false)
            setSeekBarSeek(false)
            hideControllerView()
            player.volume = TvPref.videoVolume

            addVideoInfoListener(object : VideoInfoListener {
                override fun isPlaying(playWhenReady: Boolean) {}

                override fun onPlayerError(e: ExoPlaybackException?) {
                    "错误如下：".logError()
                    e?.printStackTrace()

                    Observable.interval(1, TimeUnit.SECONDS)
                            .map { Runtime.getRuntime().exec("ping -c 1 www.baidu.com") }
                            .flatMap {
                                Observable.fromCallable {
                                    // 0表示可以ping通
                                    val type = it.waitFor() == 0
                                    "网络状态：$type".log()

                                    type
                                }
                            }
                            .filter { it }
                            .disposable(this@VideoPlayFragment)
                            .computeSwitch()
                            .doOnNext {
                                startPlayer<ExoUserPlayer>()
                                onCleared()
                            }
                            .subscribe()
                }

                override fun onPlayStart(currPosition: Long) {
                    hideControllerView()
                }

                override fun onLoadingChanged() {}

                override fun onPlayEnd() {
                    startPlayer<ExoUserPlayer>()
                }
            })
        }
    }

    private fun getUrl(): String {
        return if (arguments != null) {
            arguments?.getString(EXTRA_KEY_EVENT)!!
        } else {
            TvPref.lastTvUrl
        }
    }

    fun isPlayerValid(): Boolean {
        return ::player.isInitialized
    }

    fun getSnapshot(): Bitmap {
        val videoView = binding.videoView.playerView.videoSurfaceView as TextureView

        return videoView.getBitmap(Bitmap.createBitmap(resources.displayMetrics,
                192, 108, Bitmap.Config.ARGB_4444))
    }

    fun addVolume(): Int {
        if ((player.player.volume * 20).toInt() < 20) {
            player.player.volume += 0.05f
        }

        val volume = player.player.volume
        TvPref.videoVolume = volume

        (volume * 20).toInt().log()

        return (volume * 20).toInt()
    }

    fun subVolume(): Int {
        if ((player.player.volume * 20).toInt() > 0) {
            player.player.volume -= 0.05f
        }

        val volume = player.player.volume
        TvPref.videoVolume = volume

        (volume * 20).toInt().log()

        return (volume * 20).toInt()
    }

    companion object {
        /**
         * 根据传入的url返回fragment
         * @param url 视频流的地址
         */
        fun getFragmentByUrl(url: String): VideoPlayFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_KEY_EVENT, url)

            val fragment = VideoPlayFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}