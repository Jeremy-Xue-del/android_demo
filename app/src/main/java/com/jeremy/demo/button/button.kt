package com.jeremy.demo.button

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.jeremy.demo.R
import androidx.core.graphics.toColorInt

// 使用XML资源文件的按钮实现
//class AnimatedButton : AppCompatButton {
//    private var normalDrawable: Drawable? = null
//    private var pressedDrawable: Drawable? = null
//    private var animationDuration: Int = 200 // 0.2秒
//
//    constructor(context: Context) : super(context) {
//        init()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        init()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        init()
//    }
//
//    private fun init() {
//        // 加载drawable资源
//        normalDrawable = ContextCompat.getDrawable(context, R.drawable.normal_button)
//        pressedDrawable = ContextCompat.getDrawable(context, R.drawable.pressed_button)
//
//        // 设置初始背景
//        setBackgroundResource(R.drawable.normal_button)
//
//        // 设置文字颜色
//        setTextColor(Color.WHITE)
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                background = pressedDrawable
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                background = normalDrawable
//                performClick()
//            }
//        }
//        return super.onTouchEvent(event)
//    }
//
//    // 重写performClick方法以避免警告
//    override fun performClick(): Boolean {
//        super.performClick()
//        return true
//    }
//}

// 手动创建背景的按钮实现
class AnimatedButton : AppCompatButton {
    private var normalColor: Int = "#007AFF".toColorInt()
    private var pressedColor: Int = "#73B6FF".toColorInt()
    private var normalRadius: Float = 20f
    private var pressedRadius: Float = 50f
    private var animationDuration: Long = 200 // 0.2秒

    private lateinit var backgroundDrawable: GradientDrawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // 创建背景drawable
        backgroundDrawable = GradientDrawable().apply {
            setColor(normalColor)
            cornerRadius = normalRadius
        }

        // 设置背景
        background = backgroundDrawable
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animateBackground(pressedColor, pressedRadius)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animateBackground(normalColor, normalRadius)
            }

        }
        return super.onTouchEvent(event)
    }

    private fun animateBackground(targetColor: Int, targetRadius: Float) {
        // 获取当前颜色
        val currentColor = backgroundDrawable.color?.defaultColor ?: normalColor

        // 颜色动画
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, targetColor)
        colorAnimation.duration = animationDuration
        colorAnimation.interpolator = AccelerateDecelerateInterpolator()
        colorAnimation.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            backgroundDrawable.setColor(color)
        }
        colorAnimation.start()

        // 圆角动画
        val radiusAnimation = ValueAnimator.ofFloat(backgroundDrawable.cornerRadius, targetRadius)
        radiusAnimation.duration = animationDuration
        radiusAnimation.interpolator = AccelerateDecelerateInterpolator()
        radiusAnimation.addUpdateListener { animator ->
            val radius = animator.animatedValue as Float
            backgroundDrawable.cornerRadius = radius
        }
        radiusAnimation.start()
    }
}