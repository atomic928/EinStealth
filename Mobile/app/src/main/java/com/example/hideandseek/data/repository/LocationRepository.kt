package com.example.hideandseek.data.repository

import android.content.Context
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.LocationDao
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import kotlinx.coroutines.flow.Flow

class LocationRepository (private val context: Context) {

    private val locationDao: LocationDao by lazy { LocationRoomDatabase.getInstance(context).locationDao() }

    val allLocations: Flow<List<LocationData>> = locationDao.getAll()

    suspend fun insert(user: LocationData) {
        locationDao.insert(user)
    }

    suspend fun deleteAll() {
        locationDao.deleteAll()
    }
}