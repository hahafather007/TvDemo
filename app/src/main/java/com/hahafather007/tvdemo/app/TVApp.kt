package com.hahafather007.tvdemo.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.flurgle.blurkit.BlurKit

class TVApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)
        BlurKit.init(this)
    }
}