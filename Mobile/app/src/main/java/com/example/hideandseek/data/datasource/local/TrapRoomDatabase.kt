package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [TrapData::class], version = 1, exportSchema = false)
abstract class TrapRoomDatabase : RoomDatabase() {

    abstract fun trapDao(): TrapDao

    // アプリの起動時にデータベースを初期化する
    private class TrapRoomDatabaseCallback(
        private val scope: CoroutineScope,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val trapDao = database.trapDao()

                    // Delete all content here
                    trapDao.deleteAll()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TrapRoomDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope,
        ): TrapRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrapRoomDatabase::class.java,
                    "trap_db",
                )
                    .addCallback(TrapRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
