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
        val tv = TestVideos.videos.find { it.url == TvPref.lastTvUrl }

        if (tv != null) {
            currentTv.set(tv)
        } else {
            currentTv.set(TestVideos.videos.first())
        }

        tvList.addAll(TestVideos.videos)

        val list = mutableListOf<TvData>()
        (0..10).map {
            list.add(TvData("$it", ""))
        }
        list.add(TvData("撒大声地所撒大所多撒大所", "撒大声地所撒大所多撒大所"))

        tvList.addAll(list)

    }

    fun setCurrentTv(data: TvData) {
        currentTv.set(data)

        TvPref.lastTvUrl = currentTv.get()!!.url
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