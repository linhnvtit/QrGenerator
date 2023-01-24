package com.linhnvt.qrgenerator.ui.screen.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.linhnvt.qrgenerator.base.BaseFragment
import com.linhnvt.qrgenerator.databinding.HistoryScreenBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.ui.screen.home.HomeScreen
import com.linhnvt.qrgenerator.util.Constant
import com.linhnvt.qrgenerator.util.ScreenAnimation
import com.linhnvt.qrgenerator.util.observeNotNull
import com.linhnvt.qrgenerator.viewmodel.QrViewModel

class HistoryScreen : BaseFragment<HistoryScreenBinding>() {
    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryScreen().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val qrViewModel: QrViewModel by activityViewModels()
    private var adapter: HistoryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): HistoryScreenBinding {
        return HistoryScreenBinding.inflate(inflater, container, false)
    }

    override fun shouldShowHeader(): Boolean = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        getHeaderView().apply {
            setHeaderContent(Constant.HISTORY_TITLE, Constant.QR_CODES_TITLE)
            setLeftAction { activity?.onBackPressed() }
        }
    }

    override fun initData() {
        qrViewModel.fetchQrHistory()
        adapter = HistoryListAdapter().apply {
            historyList = qrViewModel.historyList.value ?: arrayListOf()
            itemClickCallback = { qrCode -> navigateToQrGenerateScreen(qrCode) }
        }
        binding.rvQrHistory.adapter = adapter
        binding.rvQrHistory.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun initAction() {
        qrViewModel.historyList.observeNotNull(this) { arr ->
            adapter?.updateData(arr)
        }
    }

    private fun navigateToQrGenerateScreen(qrCode: QrItem) {
        navigateToScreen(
            HomeScreen.newInstance(
                isViewMode = true,
                qrItem = qrCode
            ),
            HomeScreen::class.java.name,
            ScreenAnimation.SLIDE_RIGHT_TO_LEFT
        )
    }
}