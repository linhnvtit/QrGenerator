package com.linhnvt.qrgenerator.model

import com.linhnvt.qrgenerator.util.Constant
import java.util.*


data class QrItem(
    val type: QrType,
    val content: String,
    val rawValue: String,
    val createdDate: Date = Date(),
    val extra: String = Constant.EMPTY
) : java.io.Serializable

enum class QrType(text: String) {
    WIFI("Wifi"),
    URL("Url"),
    TEXT("Text")
}