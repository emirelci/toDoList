package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todolist.utils.DateConverter
import java.util.Date

@Entity(tableName = "calendar_dates")
@TypeConverters(DateConverter::class)
data class DateModel(
    val startDate: Date,
    val endDate: Date,
    @PrimaryKey(autoGenerate = true) val uuid: Int = 0,
)
