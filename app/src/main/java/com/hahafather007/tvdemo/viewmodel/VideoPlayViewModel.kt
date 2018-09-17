package com.hahafather007.tvdemo.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hahafather007.tvdemo.common.RxController
import com.hahafather007.tvdemo.model.data.TvData
import com.hahafather007.tvdemo.model.pref.TvPref
import com.hahafather007.tvdemo.model.service.TvService
import com.hahafather007.tvdemo.utils.asyncSwitch
import com.hahafather007.tvdemo.utils.computeSwitch
import com.hahafather007.tvdemo.utils.disposable
import com.hahafather007.tvdemo.utils.status
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class VideoPlayViewModel : RxController {
    override val rxComposite = CompositeDisposable()

    val loading = ObservableBoolean()
    /**
     * 电视节目列表
     */
    val tvList = ObservableArrayList<TvData>()
    /**
     * 当前的节目
     */
    val currentTv = ObservableField<TvData>()
    /**
     * 播放音量
     */
    val volume = ObservableInt()
    /**
     * 音量是否显示
     */
    val isVolumeShow = ObservableBoolean()
    /**
     * 节目数字是否显示
     */
    val isTvIndexShow = ObservableBoolean()
    /**
     * 节目数字
     */
    val tvIndex = ObservableField<String>()

    private val tvService = TvService()
    private var volumeTimer: Disposable? = null
    private var tvIndexTimer: Disposable? = null

    init {
        firstInit()

        volume.set((TvPref.videoVolume * 20).toInt())

        tvService.getTvList()
                .asyncSwitch()
                .disposable(this)
                .status(loading)
                .doOnSuccess {
                    TvPref.tvListJson = Gson().toJson(it)

                    firstInit()
                }
                .subscribe()
    }

    private fun firstInit() {
        if (TvPref.tvListJson != null) {
            val list = Gson().fromJson<List<TvData>>(TvPref.tvListJson,
                    object : TypeToken<List<TvData>>() {}.type)
            val tv = list.find { it.url == TvPref.lastTvUrl }

            if (tv != null) {
                setCurrentTv(tv)
            } else {
                setCurrentTv(list.first())
            }

            tvList.clear()
            tvList.addAll(list)
            tvIndex.set(tvList.indexOf(currentTv.get()).toString())
        }
    }

    fun setCurrentTv(data: TvData) {
        currentTv.set(data)

        TvPref.lastTvUrl = currentTv.get()!!.url
    }

    fun setVolume(vol: Int) {
        volumeTimer = Observable.timer(2, TimeUnit.SECONDS)
                .computeSwitch()
                .doOnSubscribe {
                    volumeTimer?.dispose()

                    volume.set(vol)
                    isVolumeShow.set(true)
                }
                .doOnNext { isVolumeShow.set(false) }
                .subscribe()
    }

    fun setTvIndex(index: Int) {
        // 防止输入3位数以上
        if (isTvIndexShow.get() && tvIndex.get()!!.length == 3) return

        tvIndexTimer = Observable.timer(2, TimeUnit.SECONDS)
                .computeSwitch()
                .doOnSubscribe {
                    tvIndexTimer?.dispose()

                    if (!isTvIndexShow.get()) {
                        isTvIndexShow.set(true)
                        tvIndex.set(index.toString())
                    } else {
                        tvIndex.set(tvIndex.get()!!.plus(index))
                    }
                }
                .doOnNext {
                    val num = tvIndex.get()!!.toInt()

                    if (num in 1..tvList.lastIndex + 1) {
                        val data = tvList.find { it.number == num }

                        if (data != null) {
                            setCurrentTv(data)
                        }
                    }

                    isTvIndexShow.set(false)
                }
                .subscribe()
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

    override fun onCleared() {
        super.onCleared()

        tvIndexTimer?.dispose()
        volumeTimer?.dispose()
    }
}