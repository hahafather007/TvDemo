package com.hahafather007.tvdemo.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.hahafather007.tvdemo.common.RxController
import com.hahafather007.tvdemo.common.TestVideos
import com.hahafather007.tvdemo.model.data.TvData
import com.hahafather007.tvdemo.model.pref.TvPref
import io.reactivex.disposables.CompositeDisposable

class VideoPlayViewModel : RxController {
    override val rxComposite = CompositeDisposable()

    /**
     * 电视节目列表
     */
    val tvList = ObservableArrayList<TvData>()
    val currentTv = ObservableField<TvData>()

    init {
        TestVideos.videos
                .find { it.url == TvPref.lastTvUrl }
                .apply { currentTv.set(this) }

        tvList.addAll(TestVideos.videos)

        val list = mutableListOf<TvData>()
        (0..10).map {
            list.add(TvData("$it", ""))
        }

        tvList.addAll(list)

    }

    /**
     * 下一曲
     */
    fun nextTv() {
        if (tvList.last() == currentTv.get()) {
            currentTv.set(tvList.first())
        } else {
            currentTv.set(tvList[tvList.indexOf(currentTv.get()) + 1])
        }

        TvPref.lastTvUrl = currentTv.get()!!.url
    }

    /**
     * 上一曲
     */
    fun lastTv() {
        if (tvList.first() == currentTv.get()) {
            currentTv.set(tvList.last())
        } else {
            currentTv.set(tvList[tvList.indexOf(currentTv.get()) - 1])
        }

        TvPref.lastTvUrl = currentTv.get()!!.url
    }
}