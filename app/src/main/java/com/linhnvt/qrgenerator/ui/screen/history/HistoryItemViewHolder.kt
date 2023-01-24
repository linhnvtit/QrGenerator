package com.linhnvt.qrgenerator.ui.screen.history

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhnvt.qrgenerator.databinding.HistoryItemBinding
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.util.DateFormatter

class HistoryItemViewHolder(private val binding: HistoryItemBinding) : ViewHolder(binding.root) {
    init {}

    fun bindData(itemData: HistoryItemData) {
        binding.tvQrType.text = itemData.qrHistory.type.name
        binding.tvHistoryContent.text = itemData.qrHistory.content
        binding.tvHistoryCreatedDate.text = DateFormatter.formatDate(itemData.qrHistory.createdDate)

        binding.root.setOnClickListener { itemData.onItemClick.invoke(itemData.qrHistory) }
    }
}

data class HistoryItemData(val qrHistory: QrItem, val onItemClick: (QrItem) -> Unit)