package com.example.hideandseek.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [UserData::class], version = 1, exportSchema = false)
abstract class UserRoomDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao

    // アプリの起動時にデータベースを初期化する
    private class UserRoomDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val userDao = database.userDao()

                    // Delete all content here
                    userDao.deleteAll()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "user_db4"
                )
                    .addCallback(UserRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}