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
        @Json(name = "Latitude") val Latitude: Double,
        @Json(name = "Longtitude") val Longtitude: Double,
        @Json(name = "Altitude") val Altitude: Double,
        @Json(name = "ObjId") val ObjId: Int
    )
}