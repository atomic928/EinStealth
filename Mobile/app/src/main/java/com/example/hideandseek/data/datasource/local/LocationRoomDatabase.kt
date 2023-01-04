package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationData::class], version = 1, exportSchema = false)
abstract class LocationRoomDatabase: RoomDatabase(), ViewModelProvider.Factory {

    abstract fun locationDao() : LocationDao

    companion object {
        private var INSTANCE: LocationRoomDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): LocationRoomDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocationRoomDatabase::class.java,
                        "location_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}