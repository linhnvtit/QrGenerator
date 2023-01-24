package com.linhnvt.qrgenerator.base.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.databinding.LoadingCirclesBinding
import com.linhnvt.qrgenerator.util.Constant

class LoadingCircles : LinearLayout {
    private var _binding: LoadingCirclesBinding? = null
    private val binding: LoadingCirclesBinding
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context?, attrs: AttributeSet?) {
        _binding = LoadingCirclesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        with(ValueAnimator.ofInt(0, 4)) {
            duration = 5000
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {

                }

                override fun onAnimationEnd(p0: Animator) {
                    startDelay = 1000
                    start()
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })
            addUpdateListener { value ->
                val index = value.animatedValue as Int
                val prevIndex = if (index == 0) 4 else index - 1

                (binding.root.getChildAt(prevIndex) as CardView)
                    .setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                (binding.root.getChildAt(index) as CardView)
                    .setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_light))
            }
            start()
        }
    }

}