package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.*
import kotlinx.coroutines.flow.Flow

class TrapRepository (private val trapDao: TrapDao) {

    val allTraps: Flow<List<TrapData>> = trapDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(trap: TrapData) {
        trapDao.insert(trap)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        trapDao.deleteAll()
    }
}