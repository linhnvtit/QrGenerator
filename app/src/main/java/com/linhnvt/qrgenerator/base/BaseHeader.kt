package com.linhnvt.qrgenerator.base

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.databinding.BaseHeaderBinding
import com.linhnvt.qrgenerator.util.Constant


open class BaseHeader : RelativeLayout {
    private var _binding: BaseHeaderBinding? = null
    private val binding: BaseHeaderBinding
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    private var shouldUseLevel1Header = false

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context?, attrs: AttributeSet?) {
        _binding = BaseHeaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setHeaderContent(title: String = Constant.EMPTY, subTitle: String = Constant.EMPTY) {
        binding.tvHeaderTitle.text = title

        if (subTitle.isNotEmpty())
            binding.tvHeaderSubTitle.text = subTitle
        else {
            if (shouldUseLevel1Header) {
                binding.tvHeaderTitle.gravity = Gravity.START
                binding.tvHeaderSubTitle.gravity = Gravity.START
            }

            binding.tvHeaderSubTitle.visibility = View.GONE
            val layout = binding.headerContainer
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout)
            constraintSet.connect(
                R.id.tvHeaderTitle,
                ConstraintSet.BOTTOM,
                R.id.ivBackIcon,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.applyTo(layout)
        }
    }

    fun setLeftAction(
        resId: Int? = R.drawable.ic_baseline_keyboard_arrow_left_24,
        onBackPressed: (() -> Unit)? = null
    ) {
        if (resId != null) {
            binding.ivBackIcon.let {
                it.setImageDrawable(ContextCompat.getDrawable(context, resId))
                it.setOnClickListener {
                    onBackPressed?.invoke()
                }
            }
        }
    }

    fun setRightAction(
        resId: Int? = R.drawable.ic_baseline_qr_code_scanner_24,
        onClick: (() -> Unit)? = null
    ) {
        if (resId != null) {
            binding.ivBaseHeaderRightIcon.visibility = View.VISIBLE
            binding.ivBaseHeaderRightIcon.let {
                it.setImageDrawable(ContextCompat.getDrawable(context, resId))
                it.setOnClickListener {
                    onClick?.invoke()
                }
            }
        }
    }

    fun hideLeftIcon(hide: Boolean) {
        binding.ivBackIcon.visibility = if (hide) View.GONE else View.VISIBLE
    }

    fun setShouldUseLevel1Header(value: Boolean) {
        this.shouldUseLevel1Header = value
    }
}