package com.linhnvt.qrgenerator.api

import com.linhnvt.qrgenerator.api.interfaces.ManifestService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getManifestService(): ManifestService {
        val baseUrl = "https://mocki.io/"
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManifestService::class.java)
    }
}