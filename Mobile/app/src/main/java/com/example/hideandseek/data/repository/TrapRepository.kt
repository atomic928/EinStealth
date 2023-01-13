package com.example.hideandseek.data.repository

import android.content.Context
import com.example.hideandseek.data.datasource.local.*
import kotlinx.coroutines.flow.Flow

class TrapRepository (private val context: Context) {

    private val trapDao: TrapDao by lazy { TrapRoomDatabase.getInstance(context).trapDao() }

    val allTraps: Flow<List<TrapData>> = trapDao.getAll()

    suspend fun insert(trap: TrapData) {
        trapDao.insert(trap)
    }

    suspend fun deleteAll() {
        trapDao.deleteAll()
    }
}