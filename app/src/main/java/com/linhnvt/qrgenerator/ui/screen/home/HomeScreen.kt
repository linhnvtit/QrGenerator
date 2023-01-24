package com.linhnvt.qrgenerator.ui.screen.home

import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.HomeScreenBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.ui.screen.qrscan.QrScanScreen
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.ScreenAnimation
import com.linhnvt.qrgenerator.util.hideKeyBoard
import com.linhnvt.qrgenerator.viewmodel.QrViewModel
import kotlinx.coroutines.runBlocking


class HomeScreen : BaseFragment<HomeScreenBinding>() {
    companion object {
        @JvmStatic
        fun newInstance(
            isViewMode: Boolean = false,
            qrItem: QrItem? = null
        ) =
            HomeScreen().apply {
                arguments = Bundle().apply {
                    putBoolean(Constant.HOME_IS_VIEW_MODE, isViewMode)
                    putSerializable(Constant.QR_CODE, qrItem)
                }
            }
    }

    private val qrViewModel: QrViewModel by activityViewModels()
    private var isViewMode: Boolean = false
    private var qrItem: QrItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isViewMode = it.getBoolean(Constant.HOME_IS_VIEW_MODE, false)
            qrItem = it.getSerializable(Constant.QR_CODE) as? QrItem
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): HomeScreenBinding {
        return HomeScreenBinding.inflate(inflater, container, false)
    }

    override fun shouldShowHeader(): Boolean = !isViewMode

    override fun initView(view: View, savedInstanceState: Bundle?) {
        if (!isViewMode)
            getHeaderView().apply {
                setShouldUseLevel1Header(true)
                setHeaderContent(Constant.HOME_TITLE)
                hideLeftIcon(hide = true)
                setRightAction {
                    navigateToScreenFromViewPager(
                        QrScanScreen.newInstance(),
                        QrScanScreen::class.java.name,
                        ScreenAnimation.FADE
                    )
                }
            }
        else if (qrItem != null) {
            val constraintLayout: ConstraintLayout = binding.root
            with(ConstraintSet()) {
                clone(constraintLayout)
                connect(
                    binding.ivQrCode.id,
                    ConstraintSet.BOTTOM,
                    binding.homeQrInfo.id,
                    ConstraintSet.TOP,
                    0
                )
                applyTo(constraintLayout)
            }

            binding.btnGenQr.visibility = View.GONE
            binding.etContent.visibility = View.GONE
            binding.homeQrInfo.visibility = View.VISIBLE
            binding.homeQrInfo.setQrItem(qrItem!!)
        }
    }

    override fun initAction() {
        binding.btnGenQr.setOnClickListener {
            hideKeyBoard(activity)
            if (binding.etContent.text.isBlank())
                Toast.makeText(context, Constant.QR_EMPTY_CONTENT_WARNING, Toast.LENGTH_SHORT)
                    .show()
            else
                genQrCode()
        }
    }

    override fun initData() {
        super.initData()
        qrViewModel.fetchQrHistory()
        if (isViewMode)
            genQrCode(shouldSaveToLocal = false)
    }

    private fun genQrCode(shouldSaveToLocal: Boolean = true) {
        val point = Point()
        val display: Display =
            (context?.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getSize(point)
        val dimen = point.x.coerceAtMost(point.y) * 4 / 5

        val qrBitmap =
            runBlocking {
                qrViewModel.createQrBitmap(
                    if (isViewMode) qrItem?.rawValue.toString() else binding.etContent.text.toString(),
                    dimen,
                    shouldSaveToLocal
                )
            }
        binding.ivQrCode.setImageBitmap(qrBitmap)
    }

}