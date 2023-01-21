package com.example.hideandseek

import android.app.Application
import com.example.hideandseek.data.AppContainer
import com.example.hideandseek.data.AppContainerImpl
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import com.example.hideandseek.data.datasource.local.TrapRoomDatabase
import com.example.hideandseek.data.datasource.local.UserRoomDatabase
import com.example.hideandseek.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl()
    }

    val mapRepository by lazy { MapRepositoryImpl() }

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val userDatabase by lazy { UserRoomDatabase.getInstance(this) }
    val userRepository by lazy { UserRepositoryImpl(userDatabase.userDao()) }

    private val trapDatabase by lazy { TrapRoomDatabase.getInstance(this) }
    val trapRepository by lazy { TrapRepositoryImpl(trapDatabase.trapDao()) }

    private val locationDatabase by lazy { LocationRoomDatabase.getInstance(this, applicationScope) }
    val locationRepository by lazy { LocationRepositoryImpl(locationDatabase.locationDao()) }
}