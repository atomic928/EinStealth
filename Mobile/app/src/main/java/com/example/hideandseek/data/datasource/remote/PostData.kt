package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class PostData {
    data class PostSpacetime(
        @Json(name = "time") val time: String,
        @Json(name = "latitude") val latitude: Float,
        @Json(name = "longitude") val longitude: Float,
        @Json(name = "altitude") val altitude: Float,
        @Json(name = "objId") val objId: Int
    )
}