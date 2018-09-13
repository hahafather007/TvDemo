package com.hahafather007.tvdemo.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

/**
 * @param v 传入一个view，获取当前截图
 * @return 返回截图的bitmap
 */
fun loadBitmapFromView(v: View): Bitmap {
    val b = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_4444)
    val c = Canvas(b)
    v.draw(c)
    return b
}