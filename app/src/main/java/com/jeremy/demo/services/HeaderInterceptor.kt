package com.jeremy.demo.services

import okhttp3.Interceptor
import okhttp3.Response

/// 请求头拦截器
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")

        // 可以在这里添加更多通用请求头
        // requestBuilder.addHeader("User-Agent", "YourApp/1.0")

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}
