package com.linhnvt.qrgenerator.ui.screen.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.linhnvt.qrgenerator.MainActivity
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.SplashScreenBinding
import com.linhnvt.qrgenerator.ui.screen.home.HomeScreen
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestFullScreen()

        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set up some animation for splash screen
        setupAnimation()

        // start main activity
        startMainActivity(2500)
    }

    private fun requestFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun setupAnimation() {
        val rotateAnimBack = ObjectAnimator.ofFloat(binding.ivSplashIcon, "rotation", 0F, 360F)
        val scaleXAnimBack = ObjectAnimator.ofFloat(binding.ivSplashIcon, "scaleX", 0.5F, 1F)
        val scaleYAnimBack = ObjectAnimator.ofFloat(binding.ivSplashIcon, "scaleY", 0.5F, 1F)
        val alphaAnimBack = ObjectAnimator.ofFloat(binding.ivSplashIcon, "alpha", 0.4F, 1F)

        rotateAnimBack.duration = 5000
        scaleXAnimBack.duration = 5000
        scaleYAnimBack.duration = 5000
        alphaAnimBack.duration = 5000

        with(AnimatorSet()) {
            play(rotateAnimBack).with(scaleXAnimBack)
                .with(scaleYAnimBack).with(alphaAnimBack)
            start()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}

                override fun onAnimationEnd(p0: Animator) {
                    removeAllListeners()
                }

                override fun onAnimationCancel(p0: Animator) {
                    removeAllListeners()
                }

                override fun onAnimationRepeat(p0: Animator) {}
            })
        }
    }

    private fun startMainActivity(delay: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(
                this@SplashScreen,
                MainActivity::class.java
            )
            startActivity(i)
            finish()
        }, delay)
    }

}

