package com.hahafather007.tvdemo.model.service.interceptor

import com.hahafather007.tvdemo.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class AppInterceptor : Interceptor {
    private val delegate: Interceptor

    init {
        if (DEBUG) {
            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY
            delegate = log
        } else {
            delegate = Interceptor { it.proceed(it.request()) }
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun intercept(chain: Interceptor.Chain?): Response {
        return delegate.intercept(chain)
    }
}