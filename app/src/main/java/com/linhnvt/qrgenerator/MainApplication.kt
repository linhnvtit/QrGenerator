package com.linhnvt.qrgenerator

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    companion object {
        private var INSTANCE: MainApplication? = null

        fun context(): Context = INSTANCE!!.applicationContext!!
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}