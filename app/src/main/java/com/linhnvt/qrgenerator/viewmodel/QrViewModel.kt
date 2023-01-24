package com.linhnvt.qrgenerator.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.repository.QrRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.collections.ArrayList

class QrViewModel : ViewModel() {
    private val qrRepository = QrRepository()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _historyList = MutableLiveData<ArrayList<QrItem>>()
    val historyList: LiveData<ArrayList<QrItem>>
        get() = _historyList

    init {
        _historyList.value = arrayListOf()
    }

    fun fetchQrHistory() {
        _historyList.value = ArrayList(qrRepository.getListQrHistory())
    }

    suspend fun createQrBitmap(
        content: String,
        dimen: Int,
        shouldSaveToLocal: Boolean = true
    ): Bitmap? {
        return qrRepository.createQrBitmap(
            content,
            dimen,
            coroutineScope.coroutineContext,
            shouldSaveToLocal
        )
    }

    fun saveQrContentToLocal(qrItem: QrItem) {
        qrRepository.saveToSharePreference(qrItem)
    }
}