package com.linhnvt.qrgenerator.ui.screen.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhnvt.qrgenerator.databinding.HistoryItemBinding
import com.linhnvt.qrgenerator.model.QrItem
import kotlin.collections.ArrayList

class HistoryListAdapter : RecyclerView.Adapter<ViewHolder>() {
    var historyList = arrayListOf<QrItem>()
    var itemClickCallback: (QrItem) -> Unit = {}

    override fun getItemCount() = historyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as HistoryItemViewHolder).bindData(
            HistoryItemData(
                historyList[position],
                itemClickCallback
            )
        )
    }

    fun updateData(arr: ArrayList<QrItem>) {
//        historyList.clear()
//        historyList.addAll(arr)
        historyList = arr
        notifyDataSetChanged()
    }
}


