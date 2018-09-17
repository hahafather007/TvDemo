package com.hahafather007.tvdemo.model.data

import com.google.gson.annotations.SerializedName

data class TvData(val number: Int,
                  @SerializedName("name")
                  val title: String,
                  val url: String)