package com.hahafather007.tvdemo.utils

import android.util.Log
import com.hahafather007.tvdemo.BuildConfig.DEBUG

private const val tag = "===========>"

fun Any?.log() {
    if (DEBUG) {
        Log.i(tag, if (this == null) "null" else "$this")
    }
}

fun Any?.logError() {
    if (DEBUG) {
        Log.e(tag, if (this == null) "null" else "$this")
    }
}