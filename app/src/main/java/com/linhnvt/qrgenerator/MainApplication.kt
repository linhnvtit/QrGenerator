package com.linhnvt.qrgenerator

import android.app.Application
import android.content.Context
import com.linhnvt.qrgenerator.tracking.Analytic
import com.linhnvt.qrgenerator.util.Info

class MainApplication : Application() {
    companion object {
        private var INSTANCE: MainApplication? = null

        fun context(): Context = INSTANCE!!.applicationContext!!
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Analytic.initialize(this)
        Info.initialize()
    }
}