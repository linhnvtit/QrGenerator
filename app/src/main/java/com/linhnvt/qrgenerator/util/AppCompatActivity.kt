package com.linhnvt.qrgenerator.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.linhnvt.qrgenerator.R

fun AppCompatActivity.navigateToScreen(
    fromFragment: Fragment,
    toFragment: Fragment,
    tag: String,
    type: ScreenAnimation,
    shouldReplacePriorFragment: Boolean,
    shouldAddToBackStack: Boolean,
    resId: Int = R.id.menu_fragment
) {
    val animArr = when (type) {
        ScreenAnimation.SLIDE_RIGHT_TO_LEFT ->
            navigateSlideRightToLeft()
        ScreenAnimation.SLIDE_LEFT_TO_RIGHT ->
            navigateSlideLeftToRight()
        ScreenAnimation.SLIDE_BOTTOM_TO_TOP ->
            navigateSlideBottomToTop()
        ScreenAnimation.SLIDE_TOP_TO_BOTTOM ->
            navigateSlideTopToBottom()
        ScreenAnimation.FADE ->
            navigateFade()
        ScreenAnimation.NONE ->
            null
    }

    supportFragmentManager.commit {
        apply {
            animArr?.let {
                setCustomAnimations(
                    animArr[0],
                    animArr[1],
                    animArr[2],
                    animArr[3],
                )
            }
        }
        hide(fromFragment)
        apply {
            if (shouldReplacePriorFragment)
                replace(resId, toFragment, tag)
            else
                add(resId, toFragment, tag)
        }
        addToBackStack(tag)
    }
}
