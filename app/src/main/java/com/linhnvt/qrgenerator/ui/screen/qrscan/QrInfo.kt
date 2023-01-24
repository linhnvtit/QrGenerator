package com.linhnvt.qrgenerator.ui.screen.qrscan

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.linhnvt.qrgenerator.MainApplication
import com.linhnvt.qrgenerator.R
import com.linhnvt.qrgenerator.databinding.QrInfoBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.model.QrType
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.copyToClipboard
import com.linhnvt.qrgenerator.util.navigateToBrowser

class QrInfo : ConstraintLayout {
    private var _binding: QrInfoBinding? = null
    private val binding: QrInfoBinding
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context!!, attrs, defStyleAttr) {
        initViews(context, attrs)
    }

    private var qrItem: QrItem? = null

    fun setQrItem(qrItem: QrItem) {
        this.qrItem = qrItem
        setUpView()
    }

    var onActionItemOnClick: (() -> Unit)? = null

    private fun initViews(context: Context?, attrs: AttributeSet?) {
        _binding = QrInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun setUpView() {
        super.onAttachedToWindow()
        if (qrItem?.type == QrType.WIFI)
            binding.tvQrInfoExtra.text = qrItem?.extra
        else
            binding.tvQrInfoExtra.visibility = View.GONE

        if (qrItem?.type == QrType.URL)
            binding.tvQrInfoContent.paintFlags =
                binding.tvQrInfoContent.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.tvQrInfoTitle.text = qrItem?.type?.name
        binding.tvQrInfoContent.text = qrItem?.content

        binding.ivQrInfoAction.setImageResource(
            when (qrItem?.type) {
                QrType.URL -> R.drawable.ic_baseline_launch_24
                else -> R.drawable.ic_baseline_content_copy_24
            }
        )

        binding.ivQrInfoAction.setOnClickListener {
            if (qrItem?.type == QrType.URL)
                MainApplication.context().navigateToBrowser(qrItem?.content ?: Constant.EMPTY)
            else {
                MainApplication.context().copyToClipboard(qrItem?.content)
                Toast.makeText(context, Constant.COPY_SUCCESSFUL, Toast.LENGTH_SHORT).show()
            }

            onActionItemOnClick?.invoke()
        }
    }
}