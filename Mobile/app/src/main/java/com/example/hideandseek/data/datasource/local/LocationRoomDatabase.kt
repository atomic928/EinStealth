package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [LocationData::class], version = 1, exportSchema = false)
abstract class LocationRoomDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    // アプリの起動時にデータベースを初期化する
    private class LocationRoomDatabaseCallback(
        private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val locationDao = database.locationDao()

                    // Delete all content here
                    locationDao.deleteAll()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocationRoomDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope,
        ): LocationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationRoomDatabase::class.java,
                    "location_db",
                )
                    .addCallback(LocationRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
