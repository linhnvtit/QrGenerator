package com.linhnvt.qrgenerator.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.core.content.ContextCompat


fun Context.copyToClipboard(text: CharSequence?) {
    if (text != null) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText(Constant.LABEL, text)
        clipboard?.setPrimaryClip(clip)
    }
}

fun Context.navigateToBrowser(url: String?) {
    if (url != null) {
        val parsedUrl =
            if (!url.startsWith(Constant.HTTPS_PREFIX) && !url.startsWith(Constant.HTTP_PREFIX))
                Constant.HTTP_PREFIX + url
            else
                url

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(parsedUrl))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ContextCompat.startActivity(this, browserIntent, null)
    }
}

