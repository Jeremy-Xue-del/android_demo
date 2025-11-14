package com.jeremy.demo.services

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class LoggingInterceptor {
    companion object {
        private const val TAG = "OkHttp"

        fun create(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor { message -> Log.d(TAG, message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
}
