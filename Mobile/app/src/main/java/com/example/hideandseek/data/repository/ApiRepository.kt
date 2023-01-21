package com.example.hideandseek.data.repository

import android.os.Build
import android.util.Log
import com.example.hideandseek.data.datasource.remote.Params
import com.example.hideandseek.data.datasource.remote.Params.Companion.BASE_URL_REAL
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.datasource.remote.RestApi
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface ApiRepository {
    suspend fun getTest(): Response<ResponseData.ResponseGetTest>

    suspend fun postStatus(id: Int, status: Int): Response<ResponseData.ResponsePost>

    suspend fun getSpacetime(time: String): Response<List<ResponseData.ResponseGetSpacetime>>

    suspend fun postSpacetime(request: PostData.PostSpacetime): Response<ResponseData.ResponsePost>
}

class ApiRepositoryImpl(
    private val restApiService: RestApi
): ApiRepository {
    override suspend fun getTest(): Response<ResponseData.ResponseGetTest> =
        restApiService.getTest()

    override suspend fun postStatus(id: Int, status: Int): Response<ResponseData.ResponsePost> =
        restApiService.postStatus(id, status)

    override suspend fun getSpacetime(time: String): Response<List<ResponseData.ResponseGetSpacetime>> =
        restApiService.getSpacetime(time)

    override suspend fun postSpacetime(request: PostData.PostSpacetime): Response<ResponseData.ResponsePost> =
        restApiService.postSpacetime(request)
}