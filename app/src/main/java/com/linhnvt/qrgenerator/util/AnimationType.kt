package com.linhnvt.qrgenerator.util

import android.annotation.SuppressLint
import com.linhnvt.qrgenerator.R

enum class ScreenAnimation {
    SLIDE_RIGHT_TO_LEFT,
    SLIDE_LEFT_TO_RIGHT,
    SLIDE_TOP_TO_BOTTOM,
    SLIDE_BOTTOM_TO_TOP,
    FADE,
    NONE
}

fun navigateSlideTopToBottom(): List<Int> {
    return listOf(
        R.anim.slide_top_to_bottom,
        R.anim.slide_top_to_bottom_out,
        R.anim.slide_bottom_to_top,
        R.anim.slide_bottom_to_top_out,
    )
}

fun navigateSlideLeftToRight(): List<Int> {
    return listOf(
        R.anim.slide_left_to_right,
        R.anim.slide_left_to_right_out,
        R.anim.slide_right_to_left,
        R.anim.slide_right_to_left_out,
    )
}

fun navigateSlideRightToLeft(): List<Int> {
    return listOf(
        R.anim.slide_right_to_left,
        R.anim.slide_right_to_left_out,
        R.anim.slide_left_to_right,
        R.anim.slide_left_to_right_out,
    )
}

fun navigateSlideBottomToTop(): List<Int> {
    return listOf(
        R.anim.slide_bottom_to_top,
        R.anim.slide_bottom_to_top_out,
        R.anim.slide_top_to_bottom,
        R.anim.slide_top_to_bottom_out,
    )
}

@SuppressLint("PrivateResource")
fun navigateFade(): List<Int> {
    return listOf(
        R.animator.fade_in,
        0,
        0,
        R.animator.fade_out,
    )
}