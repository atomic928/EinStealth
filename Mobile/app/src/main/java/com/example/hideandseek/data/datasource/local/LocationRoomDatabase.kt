package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [LocationData::class], version = 1, exportSchema = false)
abstract class LocationRoomDatabase: RoomDatabase() {

    abstract fun locationDao() : LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationRoomDatabase? = null

        fun getInstance(
            context: Context
        ): LocationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationRoomDatabase::class.java,
                    "location_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}