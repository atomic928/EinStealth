package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrapData::class], version = 1, exportSchema = false)
abstract class TrapRoomDatabase: RoomDatabase() {

    abstract fun trapDao() : TrapDao

    companion object {
        @Volatile
        private var INSTANCE: TrapRoomDatabase? = null

        fun getInstance(
            context: Context
        ): TrapRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrapRoomDatabase::class.java,
                    "trap_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}