package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class PostData {
    data class PostSpacetime(
        @Json(name = "time") val time: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longtitude") val longtitude: Double,
        @Json(name = "altitude") val altitude: Double,
        @Json(name = "obj_id") val obj_id: Int
    )
}