package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class ResponseData {
    data class ResponseGetTest(
        @Json(name = "message") val message: String
    )

    data class ResponsePost(
        @Json(name = "message") val message: String
    )

    data class ResponseGetSpacetime(
        @Json(name = "latitude") val latitude: Float,
        @Json(name = "longitude") val longitude: Float,
        @Json(name = "altitude") val altitude: Float,
        @Json(name = "objId") val objId: Int
    )
}