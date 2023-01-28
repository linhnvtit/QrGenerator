package com.linhnvt.qrgenerator.util

import com.linhnvt.qrgenerator.model.Manifest

object Info {
    private lateinit var osVersion: String
    private var osApiLevel: Int = 5
    private lateinit var deviceName: String
    private lateinit var deviceModel: String
    private lateinit var deviceProduct: String
    private var manifest: Manifest? = null

    fun initialize() {
        osVersion = System.getProperty(Constant.OS_VERSION) ?: Constant.EMPTY
        osApiLevel = android.os.Build.VERSION.SDK_INT
        deviceName = android.os.Build.DEVICE
        deviceModel = android.os.Build.MODEL
        deviceProduct = android.os.Build.PRODUCT
    }

    fun getAnalyticInfo(): String {
        return "$osVersion $osApiLevel : $deviceName $deviceModel $deviceProduct"
    }

    fun updateManifest(manifest: Manifest?) = manifest.also { this.manifest = it }

    fun getAppManifest() = manifest
}