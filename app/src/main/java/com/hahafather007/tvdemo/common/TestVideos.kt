package com.hahafather007.tvdemo.common

object TestVideos {
    val videos = HashMap<String, String>()

    init {
        videos["CCTV1高清"] = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
        videos["CCTV3高清"] = "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"
        videos["CCTV5高清"] = "http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8"
        videos["CCTV6高清"] = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"
        videos["香港卫视"] = "rtmp://live.hkstv.hk.lxdns.com/live/hks"
    }
}