package com.linhnvt.qrgenerator.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.linhnvt.qrgenerator.R

class Button : AppCompatButton {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initViews(context)
    }

    private fun initViews(context: Context?) {
        val padding8 = context?.resources?.getDimension(R.dimen.gap_8_dp)?.toInt()
        val padding16 = context?.resources?.getDimension(R.dimen.gap_24_dp)?.toInt()
        padding8?.let {
            padding16?.let { it1 -> setPadding(it1, padding8, padding16, padding8) }
        }

        setBackgroundResource(R.drawable.button)

        val textColor = ContextCompat.getColor(context!!, R.color.grey_light)
        setTextColor(textColor)
    }
}