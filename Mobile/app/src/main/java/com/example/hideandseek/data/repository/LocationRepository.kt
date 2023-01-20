package com.example.hideandseek.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.LocationDao
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import kotlinx.coroutines.flow.Flow

class LocationRepository (private val locationDao: LocationDao) {

    val allLocations: Flow<List<LocationData>> = locationDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: LocationData) {
        locationDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        locationDao.deleteAll()
    }
}