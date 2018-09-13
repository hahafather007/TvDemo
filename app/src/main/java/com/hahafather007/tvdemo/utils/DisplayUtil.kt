package com.hahafather007.tvdemo.utils

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.Window

private lateinit var screenInfo: ScreenInfo

/**
 * 获取屏幕信息
 */
fun Activity.getScreenInfo(): ScreenInfo {
    if (!::screenInfo.isInitialized) {
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        screenInfo = ScreenInfo(metric.widthPixels, metric.heightPixels, metric.density, metric.densityDpi)
    }

    return screenInfo
}

/**
 * @param width 屏幕宽度
 * @param height 屏幕高度
 * @param density 屏幕密度
 * @param dpi 屏幕的dpi
 */
data class ScreenInfo(val width: Int,
                      val height: Int,
                      val density: Float,
                      val dpi: Int)

/**
 * 获取系统状态栏高度
 */
fun Activity.getStateBarHeight(): Int {
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)

    return frame.top
}

/**
 * 获取标题栏高度
 */
fun Activity.getTitleBarHeight(): Int {
    val contentTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top

    return contentTop - getStateBarHeight()
}