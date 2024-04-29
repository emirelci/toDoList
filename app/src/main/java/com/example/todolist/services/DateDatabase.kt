package com.example.todolist.services

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolist.data.model.CalendarDateModel
import com.example.todolist.data.model.DateModel
import com.example.todolist.utils.DateConverter

@Database(entities = [DateModel::class], version = 2,exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class DateDatabase : RoomDatabase() {

    abstract fun dateDao() : CalendarDao

    companion object{

        @Volatile private var instance : DateDatabase? = null

        private val lock = Any()

        operator fun invoke(contex:Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(contex).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, DateDatabase::class.java, "calendar_dates"
        ).build()

    }
}