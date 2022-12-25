package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class ResponseData {
    data class ResponseGetTest(
        @Json(name = "message") val message: String
    )
}