package com.hahafather007.tvdemo.app

import android.app.Application
import com.chibatching.kotpref.Kotpref

class TVApp : Application() {
    override fun onCreate() {
        super.onCreate(

        Kotpref.init(this)
    }
}