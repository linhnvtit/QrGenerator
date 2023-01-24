package com.linhnvt.qrgenerator.base.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.linhnvt.qrgenerator.databinding.PointingViewBinding
import com.linhnvt.qrgenerator.util.Constant

class PointingView : RelativeLayout {
    private var _binding: PointingViewBinding? = null
    private val binding: PointingViewBinding
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context?, attrs: AttributeSet?) {
        _binding = PointingViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val animX = ObjectAnimator.ofFloat(
            binding.cvPointingViewInner,
            "scaleX",
            1F,
            4F,
        ).apply {
            duration = 1000
        }
        val animXReverse = ObjectAnimator.ofFloat(
            binding.cvPointingViewInner,
            "scaleX",
            4F,
            1F,
        ).apply {
            duration = 1000
        }
        val animY = ObjectAnimator.ofFloat(
            binding.cvPointingViewInner,
            "scaleY",
            1F,
            4F,
        ).apply {
            duration = 1000
        }
        val animYReverse = ObjectAnimator.ofFloat(
            binding.cvPointingViewInner,
            "scaleY",
            4F,
            1F,
        ).apply {
            duration = 1000
        }

        AnimatorSet().apply {
            play(animX).with(animY)
            play(animY).before(animXReverse).before(animYReverse)
            start()
            addListener(object: AnimatorListener{
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    start()
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
        }
    }
}