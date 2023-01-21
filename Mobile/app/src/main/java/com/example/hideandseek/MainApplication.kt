package com.example.hideandseek

import android.app.Application
import com.example.hideandseek.data.AppContainer
import com.example.hideandseek.data.AppContainerImpl
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import com.example.hideandseek.data.datasource.local.TrapRoomDatabase
import com.example.hideandseek.data.datasource.local.UserRoomDatabase
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.data.repository.UserRepository

class MainApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl()
    }

    private val userDatabase by lazy { UserRoomDatabase.getInstance(this) }
    val userRepository by lazy { UserRepository(userDatabase.userDao()) }

    private val trapDatabase by lazy { TrapRoomDatabase.getInstance(this) }
    val trapRepository by lazy { TrapRepository(trapDatabase.trapDao()) }

    private val locationDatabase by lazy { LocationRoomDatabase.getInstance(this) }
    val locationRepository by lazy { LocationRepository(locationDatabase.locationDao()) }
}