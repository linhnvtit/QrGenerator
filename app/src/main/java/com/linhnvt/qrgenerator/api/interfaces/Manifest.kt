package com.linhnvt.qrgenerator.api.interfaces

import com.linhnvt.qrgenerator.model.Manifest
import retrofit2.Response
import retrofit2.http.GET

interface ManifestService {
    @GET("v1/a71f5387-c5a1-4cde-8839-ff7aecbf6e0d")
    suspend fun getAppManifest(): Response<Manifest>
}