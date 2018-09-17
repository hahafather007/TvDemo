package com.hahafather007.tvdemo.model.service

import com.hahafather007.tvdemo.common.CommonUrl
import com.hahafather007.tvdemo.model.data.TvData
import com.hahafather007.tvdemo.model.service.interceptor.AppInterceptor
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class TvService {
    private val url = CommonUrl.TV_URL
    private val api: TvApi

    init {
        val client = OkHttpClient.Builder()
                .addInterceptor(AppInterceptor())
                .build()

        api = Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TvApi::class.java)
    }

    fun getTvList(): Single<List<TvData>> {
        return api.getTvList()
    }
}

interface TvApi {
    @GET("program/list")
    fun getTvList(): Single<List<TvData>>
}