package com.hahafather007.tvdemo.model.pref

import com.chibatching.kotpref.KotprefModel

object TvPref : KotprefModel() {
    override val kotprefName = "tv_pref"

    var lastTvUrl: String by stringPref(default = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8")
    var videoVolume: Float by floatPref(default = 0.5f)
}