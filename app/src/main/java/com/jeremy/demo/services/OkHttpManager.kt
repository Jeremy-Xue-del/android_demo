package com.jeremy.demo.services

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * OkHttpClient单例封装类
 */
class OkHttpManager private constructor(context: Context) {
    private val okHttpClient: OkHttpClient

    companion object {
        private const val DEFAULT_CONNECT_TIMEOUT = 10L
        private const val DEFAULT_READ_TIMEOUT = 10L
        private const val DEFAULT_WRITE_TIMEOUT = 10L
        private const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB
        private val JSON = "application/json; charset=utf-8".toMediaType()

        @Volatile
        private var INSTANCE: OkHttpManager? = null

        fun getInstance(context: Context): OkHttpManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OkHttpManager(context).also { INSTANCE = it }
            }
        }

        fun getInstance(): OkHttpManager {
            return INSTANCE ?: throw IllegalStateException(
                "OkHttpManager not initialized. Call getInstance(Context) first."
            )
        }
    }

    init {
        val appContext = context.applicationContext
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .cache(Cache(appContext.cacheDir, CACHE_SIZE))
            .addInterceptor(LoggingInterceptor.create())
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(ResultInterceptor())

        okHttpClient = builder.build()
    }

    fun getClient(): OkHttpClient = okHttpClient

    /**
     * 发送GET请求
     * @param url 请求地址
     * @return Response对象
     */
    @Throws(IOException::class)
    fun get(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送带请求头的GET请求
     * @param url 请求地址
     * @param headers 请求头
     * @return Response对象
     */
    @Throws(IOException::class)
    fun get(url: String, headers: Map<String, String>): Response {
        val builder = Request.Builder().url(url)
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }
        val request = builder.build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送异步GET请求
     * @param url 请求地址
     * @param callback 回调函数
     */
    fun get(url: String, callback: Callback) {
        val request = Request.Builder()
            .url(url)
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    /**
     * 发送带请求头的异步GET请求
     * @param url 请求地址
     * @param headers 请求头
     * @param callback 回调函数
     */
    fun get(url: String, headers: Map<String, String>, callback: Callback) {
        val builder = Request.Builder().url(url)
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }
        val request = builder.build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    /**
     * 发送POST请求 (JSON数据)
     * @param url 请求地址
     * @param json JSON数据
     * @return Response对象
     */
    @Throws(IOException::class)
    fun post(url: String, json: String): Response {
        val body = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送带请求头的POST请求 (JSON数据)
     * @param url 请求地址
     * @param json JSON数据
     * @param headers 请求头
     * @return Response对象
     */
    @Throws(IOException::class)
    fun post(url: String, json: String, headers: Map<String, String>): Response {
        val body = json.toRequestBody(JSON)
        val builder = Request.Builder()
            .url(url)
            .post(body)
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }
        val request = builder.build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送异步POST请求 (JSON数据)
     * @param url 请求地址
     * @param json JSON数据
     * @param callback 回调函数
     */
    fun post(url: String, json: String, callback: Callback) {
        val body = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    /**
     * 发送带请求头的异步POST请求 (JSON数据)
     * @param url 请求地址
     * @param json JSON数据
     * @param headers 请求头
     * @param callback 回调函数
     */
    fun post(url: String, json: String, headers: Map<String, String>, callback: Callback) {
        val body = json.toRequestBody(JSON)
        val builder = Request.Builder()
            .url(url)
            .post(body)
        headers.forEach { (key, value) ->
            builder.addHeader(key, value)
        }
        val request = builder.build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    /**
     * 发送POST请求 (表单数据)
     * @param url 请求地址
     * @param params 表单参数
     * @return Response对象
     */
    @Throws(IOException::class)
    fun post(url: String, params: Map<String, String>): Response {
        val builder = FormBody.Builder()
        params.forEach { (key, value) ->
            builder.add(key, value)
        }
        val body = builder.build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送带请求头的POST请求 (表单数据)
     * @param url 请求地址
     * @param params 表单参数
     * @param headers 请求头
     * @return Response对象
     */
    @Throws(IOException::class)
    fun post(url: String, params: Map<String, String>, headers: Map<String, String>): Response {
        val formBuilder = FormBody.Builder()
        params.forEach { (key, value) ->
            formBuilder.add(key, value)
        }
        val body = formBuilder.build()

        val requestBuilder = Request.Builder()
            .url(url)
            .post(body)
        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()
        return okHttpClient.newCall(request).execute()
    }

    /**
     * 发送异步POST请求 (表单数据)
     * @param url 请求地址
     * @param params 表单参数
     * @param callback 回调函数
     */
    fun post(url: String, params: Map<String, String>, callback: Callback) {
        val builder = FormBody.Builder()
        params.forEach { (key, value) ->
            builder.add(key, value)
        }
        val body = builder.build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }

    /**
     * 发送带请求头的异步POST请求 (表单数据)
     * @param url 请求地址
     * @param params 表单参数
     * @param headers 请求头
     * @param callback 回调函数
     */
    fun post(
        url: String,
        params: Map<String, String>,
        headers: Map<String, String>,
        callback: Callback
    ) {
        val formBuilder = FormBody.Builder()
        params.forEach { (key, value) ->
            formBuilder.add(key, value)
        }
        val body = formBuilder.build()

        val requestBuilder = Request.Builder()
            .url(url)
            .post(body)
        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }
        val request = requestBuilder.build()
        okHttpClient.newCall(request).enqueue(callback)
    }
}
