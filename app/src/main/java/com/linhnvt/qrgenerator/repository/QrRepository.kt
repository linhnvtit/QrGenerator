package com.linhnvt.qrgenerator.repository

import android.graphics.Bitmap
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.gson.Gson
import com.linhnvt.qrgenerator.model.QrItem
import com.linhnvt.qrgenerator.model.QrType
import com.linhnvt.qrgenerator.util.CommonSharedPreference
import com.linhnvt.qrgenerator.util.Constant
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

class QrRepository {
    suspend fun createQrBitmap(
        content: String,
        dimen: Int,
        coroutineContext: CoroutineContext,
        shouldSaveToLocal: Boolean
    ): Bitmap? {
        return withContext(coroutineContext) {
            val qrEncoder =
                QRGEncoder(content, null, QRGContents.Type.TEXT, dimen)

            if (shouldSaveToLocal)
                launch {
                    saveToSharePreference(QrItem(QrType.TEXT, content, content))
                }

            return@withContext qrEncoder.bitmap
        }
    }

    fun saveToSharePreference(qrItem: QrItem) {
        try {
            val history = getListQrHistory()

            // add new item to the start of array
            history.add(0, qrItem)
            val jsonStr = Gson().toJson(history)

            CommonSharedPreference.getInstance().setString(Constant.QR_HISTORY_KEY, jsonStr)
        } catch (e: Exception) {
            Log.e("QrRepository", e.toString())
        }
    }

    fun getListQrHistory(): MutableList<QrItem> {
        val strList = CommonSharedPreference.getInstance().getString(Constant.QR_HISTORY_KEY)
        return if (strList.isBlank())
            mutableListOf()
        else Gson().fromJson(strList, Array<QrItem>::class.java).toMutableList()
    }
}