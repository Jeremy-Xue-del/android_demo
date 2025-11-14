package com.jeremy.demo


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.jeremy.demo.databinding.ActivityHttpTestBinding
import com.jeremy.demo.services.CaiYunApi
import com.jeremy.demo.services.OkHttpManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class HttpTest : AppCompatActivity() {

    private lateinit var binding: ActivityHttpTestBinding

    private lateinit var httpButton: Button

    private lateinit var httpText: TextView

    private lateinit var back: Button

    private lateinit var syncTest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHttpTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        httpButton = binding.testButton
        httpText = binding.resultText
        back = binding.backButton
        syncTest = binding.syncTestButton

        // 设置按钮点击事件
        binding.testButton.setOnClickListener {
            testCaiYunWeatherAsync()
        }

        binding.syncTestButton.setOnClickListener {
            testCaiYunWeatherSync()
        }



        binding.backButton.setOnClickListener {
            finish() // 返回上一个页面
        }
    }

    private fun testCaiYunWeatherAsync() {
        binding.resultText.text = "正在同步获取彩云天气数据..."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = CaiYunApi.getInstance().getRealtimeWeather("139.6917", "35.6895")
               if (response.isSuccessful){
                   val responseBody = response.body?.string() ?: "空响应"
                   withContext(Dispatchers.Main) {
                       binding.resultText.text = "同步获取天气数据成功:\n$responseBody"
                       Log.d("CaiYunTest", "同步获取天气数据成功: $responseBody")
                   }
               }else{
                   withContext(Dispatchers.Main) {
                       binding.resultText.text = "同步获取天气数据失败，错误码: ${response.code}"
                       Log.e("CaiYunTest", "同步获取天气数据失败，错误码: ${response.code}")
                   }
               }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.resultText.text = "获取天气数据失败: ${e.message}"
                    Log.e("CaiYunTest", "获取天气数据失败", e)
                }
            }
        }
    }


    private fun testCaiYunWeatherSync() {
        // 更新UI状态
        runOnUiThread {
            binding.resultText.text = "正在异步获取彩云天气数据..."
        }

        // 使用彩云天气API获取东京的天气数据（异步方式）
        try {
            CaiYunApi.getInstance().getRealtimeWeather("139.6917", "35.6895", object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        binding.resultText.text = "异步获取天气数据失败: ${e.message}"
                        Log.e("CaiYunTest", "异步获取天气数据失败", e)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string() ?: "空响应"
                            runOnUiThread {
                                binding.resultText.text = "异步获取天气数据成功:\n$responseBody"
                                Log.d("CaiYunTest", "异步获取天气数据成功: $responseBody")
                            }
                        } else {
                            runOnUiThread {
                                binding.resultText.text = "异步获取天气数据失败，错误码: ${response.code}"
                                Log.e("CaiYunTest", "异步获取天气数据失败，错误码: ${response.code}")
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            binding.resultText.text = "处理响应时出错: ${e.message}"
                            Log.e("CaiYunTest", "处理响应时出错", e)
                        }
                    } finally {
                        response.close()
                    }
                }
            })
        } catch (e: IllegalStateException) {
            binding.resultText.text = "错误: ${e.message}"
        }
    }
}