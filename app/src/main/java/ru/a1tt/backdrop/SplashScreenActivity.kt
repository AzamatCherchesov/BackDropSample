package ru.a1tt.backdrop

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {
    companion object {
        const val ANIMATION_DURATION = 1000L
    }

    private lateinit var leftBannerTextView: TextView
    private lateinit var rightBannerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            R.anim.fade_in,
            R.anim.fade_out
        )
        setContentView(R.layout.splash_screen)

        leftBannerTextView = findViewById(R.id.left_banner_text_view)
        rightBannerTextView = findViewById(R.id.right_banner_text_view)
        leftBannerTextView.post {
            startLeftBannerAnimation()
        }
        rightBannerTextView.post {
            startRightBannerAnimation()
        }
    }

    private fun startLeftBannerAnimation() {
        val leftBannerPaint = leftBannerTextView.paint
        val leftCharWidth = leftBannerPaint.measureText(leftBannerTextView.text, 0, 1)
        val leftAnimation = TranslateAnimation(
            0.0F,
            leftBannerTextView.measuredWidth.toFloat(),
            0F,
            0F
        )
        leftAnimation.duration = ANIMATION_DURATION
        leftAnimation.fillAfter = true
        leftBannerTextView.startAnimation(leftAnimation)

        leftAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

    private fun startRightBannerAnimation() {
        val rightBannerText: Paint = rightBannerTextView.paint
        val lastCharWidth = rightBannerText.measureText(
            rightBannerTextView.text,
            rightBannerTextView.length() - 1,
            rightBannerTextView.length()
        )
        val rightAnimation = TranslateAnimation(
            0.0F,
            - rightBannerTextView.measuredWidth.toFloat(),
            0F,
            0F
        )
        rightAnimation.duration = ANIMATION_DURATION
        rightAnimation.fillAfter = true
        rightBannerTextView.startAnimation(rightAnimation)
    }
}