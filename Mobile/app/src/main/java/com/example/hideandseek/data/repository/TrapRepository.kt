package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.*
import kotlinx.coroutines.flow.Flow

interface TrapRepository {
    val allTraps: Flow<List<TrapData>>

    suspend fun insert(trap: TrapData)

    suspend fun deleteAll()
}

class TrapRepositoryImpl(
    private val trapDao: TrapDao,
) : TrapRepository {

    override val allTraps: Flow<List<TrapData>>
        get() = trapDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(trap: TrapData) {
        trapDao.insert(trap)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteAll() {
        trapDao.deleteAll()
    }
}
