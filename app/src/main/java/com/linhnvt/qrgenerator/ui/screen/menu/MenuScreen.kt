package com.linhnvt.qrgenerator.ui.screen.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.MenuScreenBinding
import com.linhnvt.qrgenerator.ui.screen.history.HistoryScreen
import com.linhnvt.qrgenerator.ui.screen.qrscan.QrScanScreen
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.ScreenAnimation

class MenuScreen : BaseFragment<MenuScreenBinding>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            MenuScreen().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MenuScreenBinding {
        return MenuScreenBinding.inflate(inflater, container, false)
    }

    override fun initAction() {
        binding.scanQrItem.setOnClickListener {
            navigateToQrScanScreen()
        }

        binding.historyItem.setOnClickListener {
             navigateToHistoryScreen()
        }
    }

    override fun shouldShowHeader(): Boolean = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        getHeaderView().apply {
            setHeaderContent(Constant.MENU_TITLE)
            hideLeftIcon(hide = true)
        }
    }

    private fun navigateToHistoryScreen() {
        navigateToScreenFromViewPager(
            HistoryScreen.newInstance(),
            HistoryScreen::class.java.name,
            ScreenAnimation.SLIDE_RIGHT_TO_LEFT
        )
    }

    private fun navigateToQrScanScreen() {
        navigateToScreenFromViewPager(
            QrScanScreen.newInstance(),
            QrScanScreen::class.java.name,
            ScreenAnimation.SLIDE_RIGHT_TO_LEFT
        )
    }
}