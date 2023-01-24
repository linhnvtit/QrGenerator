package com.linhnvt.qrgenerator.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {
    fun formatDate(date: Date): String {
        val pattern = "dd-MM-yyyy hh:mm a"
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(date)
    }
}