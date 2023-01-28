package com.linhnvt.qrgenerator.tracking

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

object Analytic {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun initialize(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun getInstance() = firebaseAnalytics
}