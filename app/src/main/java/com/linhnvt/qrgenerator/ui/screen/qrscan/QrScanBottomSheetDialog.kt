package com.linhnvt.qrgenerator.ui.screen.qrscan

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.linhnvt.qrgenerator.databinding.QrScanBottomSheetBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.util.Constant

class QrScanBottomSheetDialog(
    private val qrItem: QrItem,
    private val onDismissCallback: () -> Unit,
    private val onSaveQrItem: () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: QrScanBottomSheetBinding? = null
    private val binding: QrScanBottomSheetBinding
        get() = _binding ?: throw IllegalStateException(Constant.BINDING_INVALID_WARNING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QrScanBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.qrBottomSheetInfo.setQrItem(qrItem)
        binding.qrBottomSheetInfo.onActionItemOnClick = {
            onSaveQrItem()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        onDismissCallback.invoke()
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback.invoke()
        super.onDismiss(dialog)
    }
}