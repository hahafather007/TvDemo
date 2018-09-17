package com.hahafather007.tvdemo.common

import com.hahafather007.tvdemo.model.data.TvData

object TestVideos {
    val videos = mutableListOf<TvData>()

    init {
        videos.add(TvData(1, "CCTV1高清", "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"))
        videos.add(TvData(2, "CCTV3高清", "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"))
        videos.add(TvData(3, "CCTV5高清", "http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8"))
        videos.add(TvData(4, "CCTV6高清", "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"))
        videos.add(TvData(5, "香港卫视", "rtmp://live.hkstv.hk.lxdns.com/live/hks"))
        videos.add(TvData(6, "亚大第一卫视", "rtmp://v1.one-tv.com:1935/live/mpegts.stream"))
    }
}