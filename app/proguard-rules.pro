# 视频播放器
-dontwarn chuangyuan.ycj.**
-keep class chuangyuan.ycj.** { *;}

# 高斯模糊
-keep class jp.wasabeef.blurry.** { *; }
-dontwarn android.support.v8.renderscript.*
-keepclassmembers class android.support.v8.renderscript.RenderScript {
  native *** rsn*(...);
  native *** n*(...);
}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keepattributes *Annotation*
-dontwarn javax.**

#data类
-keep public class com.hahafather007.tvdemo.model.data.** {public private protected *;}

#RxJava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#Gson
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}
-keep class sun.misc.Unsafe { *; }
-keepattributes Signature
-keepattributes *Annotation*

-dontwarn com.google.android.exoplayer2.**
-keep class com.google.android.exoplayer2.** { *;}
-keepclasseswithmembers class com.google.android.exoplayer2.ext.ffmpeg {
   native <methods>;
}