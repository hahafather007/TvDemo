package com.hahafather007.tvdemo.common

import com.hahafather007.tvdemo.model.data.TvData

object TestVideos {
    val videos = mutableListOf<TvData>()

    init {
        videos.add(TvData("CCTV1高清", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"))
        videos.add(TvData("CCTV3高清", "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"))
        videos.add(TvData("CCTV5高清", "http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8"))
        videos.add(TvData("CCTV6高清", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"))
        videos.add(TvData("香港卫视", "rtmp://live.hkstv.hk.lxdns.com/live/hks"))
        videos.add(TvData("亚大第一卫视", "rtmp://v1.one-tv.com:1935/live/mpegts.stream"))
    }
}