package com.jeremy.demo.services

import android.util.Log
import com.jeremy.demo.model.HttpModel
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import com.google.gson.Gson
import java.io.IOException

class ResultInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val contentType = response.body?.contentType()
        val bodyString = response.body?.string()

        return if (bodyString != null && contentType != null) {
            try {
                // 将响应解析为HttpModel类型
                val gson = Gson()
                val httpModel = gson.fromJson(bodyString, HttpModel::class.java)

                // 检查响应是否成功
                if (httpModel.status == "ok") {
                    // 将响应结果替换为result部分
                    val resultString = gson.toJson(httpModel.result)
                    Log.d("响应拦截", "intercept: $resultString")

                    // 创建新的响应，只包含result部分
                    response.newBuilder()
                        .body(resultString.toResponseBody(contentType))
                        .build()
                } else {
                    // 如果响应不成功，返回原始响应
                    response.newBuilder()
                        .body(bodyString.toResponseBody(contentType))
                        .build()
                }
            } catch (e: Exception) {
                // 打印错误信息以便调试
                e.printStackTrace()
                Log.e("ResultInterceptor", "JSON解析错误: ${e.message}")
                // 如果解析失败，返回原始响应
                response.newBuilder()
                    .body(bodyString.toResponseBody(contentType))
                    .build()
            }
        } else {
            response
        }
    }
}
