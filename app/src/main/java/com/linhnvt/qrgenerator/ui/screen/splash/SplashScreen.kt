package com.linhnvt.qrgenerator.ui.screen.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.linhnvt.qrgenerator.MainActivity
import com.linhnvt.qrgenerator.api.RetrofitHelper
import com.linhnvt.qrgenerator.databinding.SplashScreenBinding
import com.linhnvt.qrgenerator.util.Info
import kotlinx.coroutines.*
import retrofit2.Retrofit

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding

    private var doneFirstAnimation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestFullScreen()

        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) { }

        // set up some animation for splash screen
        setupAnimation()

        // fetching app manifest data
        fetchManifestSetting()
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
                    startMainActivity()
                    doneFirstAnimation = true
                    removeAllListeners()
                }

                override fun onAnimationCancel(p0: Animator) {
                    removeAllListeners()
                }

                override fun onAnimationRepeat(p0: Animator) {}
            })
        }
    }

    private fun fetchManifestSetting() {
        lifecycleScope.launch(Dispatchers.Default) {
            val manifest = RetrofitHelper.getManifestService().getAppManifest()
            Info.updateManifest(manifest.body())

            if (doneFirstAnimation)
                launch(Dispatchers.Main) {
                    startMainActivity()
                }
        }
    }

    private fun startMainActivity() {
        val i = Intent(
            this@SplashScreen,
            MainActivity::class.java
        )
        startActivity(i)
        finish()
    }

}

