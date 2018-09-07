package com.hahafather007.tvdemo.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager

/**
 * 检测网络是否可用
 */
fun isNetworkAvailable(context: Context): Boolean {
    val manager = context.applicationContext.getSystemService(CONNECTIVITY_SERVICE)
            as ConnectivityManager

    val networkInfo = manager.activeNetworkInfo

    return networkInfo != null && networkInfo.isAvailable
}
