package com.example.todolist.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist.data.model.CalendarDateModel
import com.example.todolist.data.model.DateModel

@Dao
interface CalendarDao {

    @Insert
    suspend fun insert(calendarDateModel: DateModel)

    @Query("SELECT * FROM calendar_dates")
    suspend fun getAllDates() : List<DateModel>

    @Query("SELECT * FROM calendar_dates WHERE uuid = :dateId")
    suspend fun getDate(dateId : Int) : DateModel

    @Query("DELETE FROM calendar_dates")
    suspend fun deleteAllDates()

}