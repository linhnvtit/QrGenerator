package com.linhnvt.qrgenerator.util

import android.preference.PreferenceManager
import com.linhnvt.qrgenerator.MainApplication

class CommonSharedPreference {
    companion object {
        private val INSTANCE = CommonSharedPreference()

        fun getInstance() = INSTANCE
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.context())

    fun getFloat(key: String): Float {
        try {
            return sharedPreferences.getFloat(key, 0F)
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun getString(key: String): String {
        try {
            return sharedPreferences.getString(key, Constant.EMPTY) ?: Constant.EMPTY
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun getBoolean(key: String): Boolean {
        try {
            return sharedPreferences.getBoolean(key, false)
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun getInt(key: String): Int {
        try {
            return sharedPreferences.getInt(key, 0)
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun setFloat(key: String, value: Float) {
        try {
            sharedPreferences.edit().putFloat(key, value).apply()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun setString(key: String, content: String) {
        try {
            sharedPreferences.edit().putString(key, content).apply()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun setBoolean(key: String, value: Boolean) {
        try {
            sharedPreferences.edit().putBoolean(key, value).apply()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }

    fun setInt(key: String, value: Int) {
        try {
            sharedPreferences.edit().putInt(key, value).apply()
        } catch (e: NumberFormatException) {
            throw ClassCastException(e.message)
        }
    }
}