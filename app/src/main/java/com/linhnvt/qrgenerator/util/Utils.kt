package com.linhnvt.qrgenerator.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyBoard(activity: Activity?) {
    val view: View? = activity?.currentFocus
    view?.apply {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun showKeyBoard(activity: Activity?) {
    val view: View? = activity?.currentFocus
    view?.apply {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}

fun getDeviceWidthPixel() = Resources.getSystem().displayMetrics.widthPixels

fun getDeviceHeightPixel() = Resources.getSystem().displayMetrics.heightPixels
