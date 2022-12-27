package com.example.hideandseek.data.repository

import android.os.Build
import android.util.Log
import com.example.hideandseek.data.datasource.remote.Params
import com.example.hideandseek.data.datasource.remote.Params.Companion.BASE_URL
import com.example.hideandseek.data.datasource.remote.Params.Companion.BASE_URL_REAL
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.datasource.remote.RestApi
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiRepository {
    // 10秒でタイムアウトとなるように設定
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val service: RestApi = Retrofit.Builder()
        .baseUrl(setBaseUrl())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(RestApi::class.java)

    suspend fun getTest(): Response<ResponseData.ResponseGetTest> =
        service.getTest()

    suspend fun postStatus(id: Int, status: Int): Response<ResponseData.ResponsePost> =
        service.postStatus(id, status)

    suspend fun getSpacetime(time: String): Response<List<ResponseData.ResponseGetSpacetime>> =
        service.getSpacetime(time)

    suspend fun postSpacetime(request: PostData.PostSpacetime): Response<ResponseData.ResponsePost> =
        service.postSpacetime(request)


    companion object Factory {
        val instance: ApiRepository
            @Synchronized get() {
                return ApiRepository()
            }
    }

    private fun setBaseUrl(): String {
        return if (isEmulator()) {
            Params.BASE_URL_EMULATOR
        } else {
            BASE_URL_REAL
        }
    }

    private fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MODEL.contains("sdk_phone_armv7") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                Build.PRODUCT == "google_sdk" ||
                Build.PRODUCT == "sdk_gphone_x86"
    }
}