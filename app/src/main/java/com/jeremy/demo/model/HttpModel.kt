package com.jeremy.demo.model

import com.google.gson.annotations.SerializedName

data class HttpModel(
    @SerializedName("api_status")
    val apiStatus: String?,// 接口状态
    @SerializedName("api_version")
    val apiVersion: String?,/// 接口版本
    val lang: String?,/// 语言
    val location: List<Double>?,
    val result: Any?,/// 结果
    @SerializedName("server_time")
    val serverTime: Int?,/// 服务器时间
    val status: String?,/// 状态
    val timezone: String?,/// 时区
    val tzshift: Int?,/// 时区偏移
    val unit: String?///  单位
)