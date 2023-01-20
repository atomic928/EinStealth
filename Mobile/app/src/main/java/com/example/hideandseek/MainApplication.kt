package com.example.hideandseek

import android.app.Application
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import com.example.hideandseek.data.repository.LocationRepository

class MainApplication: Application() {
    val database by lazy { LocationRoomDatabase.getInstance(this) }
    val repository by lazy { LocationRepository(database.locationDao()) }
}