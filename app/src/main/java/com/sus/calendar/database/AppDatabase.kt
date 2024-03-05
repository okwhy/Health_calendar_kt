package com.sus.calendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.sus.calendar.daos.DateDao
import com.sus.calendar.daos.NoteDao
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.Note

@Database(entities = [DateSQL::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dateDao(): DateDao?
    abstract fun noteDao(): NoteDao?

    companion object {
        private const val DB_NAME = "calender_db"
        private var instance: AppDatabase? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance =
                    databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance
        }
    }
}
