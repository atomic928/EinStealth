package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrapData::class], version = 1, exportSchema = false)
abstract class TrapRoomDatabase: RoomDatabase(), ViewModelProvider.Factory {

    abstract fun trapDao() : TrapDao

    companion object {
        private var INSTANCE: TrapRoomDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): TrapRoomDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TrapRoomDatabase::class.java,
                        "trap_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}