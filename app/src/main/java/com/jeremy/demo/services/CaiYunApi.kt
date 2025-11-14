package com.jeremy.demo.services

import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * 彩云天气API封装类
 */
class CaiYunApi {
    companion object {
        @Volatile
        private var INSTANCE: CaiYunApi? = null

        fun getInstance(): CaiYunApi {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CaiYunApi().also { INSTANCE = it }
            }
        }
    }

    /**
     * 获取彩云天气实时数据
     * @param longitude 经度
     * @param latitude 纬度
     * @return Response对象
     */
    @Throws(IOException::class)
    fun getRealtimeWeather(longitude: String, latitude: String): Response {
        val url = "${HttpConfig.CAIYUN_BASE_URL}${HttpConfig.CAIYUN_TOKEN}/$longitude,$latitude/realtime"
        val headers = mapOf("Accept" to "application/json")
        return OkHttpManager.getInstance().get(url, headers)
    }

    /**
     * 异步获取彩云天气实时数据
     * @param longitude 经度
     * @param latitude 纬度
     * @param callback 回调函数
     */
    fun getRealtimeWeather(longitude: String, latitude: String, callback: Callback) {
        try {
            val url = "${HttpConfig.CAIYUN_BASE_URL}${HttpConfig.CAIYUN_TOKEN}/$longitude,$latitude/realtime"
            val headers = mapOf("Accept" to "application/json")
            OkHttpManager.getInstance().get(url, headers, callback)
        } catch (e: Exception) {
            throw e
        }
    }
}
