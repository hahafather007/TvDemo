# 视频播放器
-dontwarn chuangyuan.ycj.**
-keep class chuangyuan.ycj.** { *;}

# 高斯模糊
-keep class com.wonderkiln.blurkit.** { *; }
-dontwarn android.support.v8.renderscript.*
-keepclassmembers class android.support.v8.renderscript.RenderScript {
  native *** rsn*(...);
  native *** n*(...);
}
