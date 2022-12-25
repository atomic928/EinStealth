package com.example.hideandseek.data.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {
    @GET("ping")
    suspend fun getTest(): Response<ResponseData.ResponseGetTest>
}