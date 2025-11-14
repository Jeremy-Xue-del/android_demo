package com.jeremy.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.jeremy.demo.button.AnimatedButton
import com.jeremy.demo.databinding.ActivityMainBinding
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jeremy.demo.database.AppDatabase
import com.jeremy.demo.database.User
import com.jeremy.demo.services.OkHttpManager
import com.jeremy.demo.util.SpUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    lateinit var account: EditText

    lateinit var password: EditText

    lateinit var loginButton: AnimatedButton

    lateinit var registerButton: AnimatedButton

    private lateinit var binding: ActivityMainBinding

    private lateinit var database: AppDatabase

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "页面创建成功")
        enableEdgeToEdge()
        // 初始化 MMKV，指定存储路径（可选，默认在 app 的 files/mmkv 目录）
        val rootDir = SpUtils.getInstance().initialize(this)
        Log.d(tag, "MMKV 存储路径：$rootDir")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        account = binding.inputFields.username
        password = binding.inputFields.password
        loginButton = binding.actionButtons.logoButton
        registerButton = binding.actionButtons.registerButton

        /// 初始化数据库
        database = AppDatabase.getDatabase(this)

        // 初始化OkHttpManager
        OkHttpManager.getInstance(this)

        /// 设置登录按钮点击事件
        loginButton.setOnClickListener {
            println("点击登录")
            login()
        }

        /// 设置注册按钮点击事件
        registerButton.setOnClickListener {
            println("点击注册")
            register()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "页面启动成功")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "页面恢复成功")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "页面暂停成功")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "页面停止成功")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "页面销毁成功")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "页面重新启动成功")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = android.graphics.Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun login() {
        // 跳转到HttpTest页面，当前页面不关闭
        val intent = Intent(this, HttpTest::class.java)
        startActivity(intent)
    }

//    /// 点击登录
//    private fun login() {
//        val username = account.text.toString().trim()
//        val password = password.text.toString().trim()
//        if (username.isEmpty()) {
//            showToast("请输入账号")
//            return
//        }
//        if (password.isEmpty()) {
//            showToast("请输入密码")
//            return
//        }
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val user = database.userDao().getUserByUsername(username)
//                withContext(Dispatchers.Main) {
//                    if (user != null) {
//                        if (user.password == password) {
//                            showToast("登录成功")
//                        } else {
//                            showToast("密码错误")
//                        }
//                    } else {
//                        showToast("用户不存在,请注册")
//                    }
//
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    showToast("登录失败: ${e.message}")
//                }
//            }
//        }
//    }

    private fun register() {
        val username = account.text.toString().trim()
        val password = password.text.toString().trim()
        if (username.isEmpty()) {
            showToast("请输入账号")
            return
        }
        if (password.isEmpty() || password.length < 8) {
            showToast("请输入密码")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = database.userDao().getUserByUsername(username)
                if (user?.userName == username) {
                    withContext(Dispatchers.Main) {
                        showToast("用户已存在")
                    }

                } else {
                    // 生成uid
                    val uid:Int = generateUserIdFromUUID()
//                    val uid = 1
                    // 插入用户
                    val user = User(uid, username, password)
                    database.userDao().insertUser(user)
                    withContext(Dispatchers.Main) {
                        showToast("注册成功")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("注册失败: ${e.message}")
                }
            }
        }
    }

    // 生成用户ID
    fun generateUserIdFromUUID(): Int {
        // 生成UUID
        val uuid = java.util.UUID.randomUUID().toString()

        // 使用MD5对UUID进行哈希
        val md5 = java.security.MessageDigest.getInstance("MD5")
        val digest = md5.digest(uuid.toByteArray())

        // 将前4个字节转换为整数
        var result = 0
        for (i in 0 until 4) {
            result = result shl 8 or (digest[i].toInt() and 0xFF)
        }

        // 确保结果为正数
        return abs(result)
    }

    private fun showToast(toast: String) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }
}