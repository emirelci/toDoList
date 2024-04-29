package com.example.todolist.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.todolist.adapter.CalendarAdapter
import com.example.todolist.adapter.CalendarToAdapter
import com.example.todolist.data.model.CalendarDateModel
import com.example.todolist.data.model.DateModel
import com.example.todolist.services.DateDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CreateNewTaskViewModel(application: Application) : BaseViewModel(application) {

    private val _startDate = MutableLiveData<Date>()
    val startDate: LiveData<Date> = _startDate

    private val _endDate = MutableLiveData<Date>()
    val endDate: LiveData<Date> = _endDate

    fun setStartDate(date: Date) {
        _startDate.value = date
    }

    fun setEndDate(date: Date) {
        _endDate.value = date
    }

    fun processSelectedDates(startDate: Date, endDate: Date, context:Context) {
        launch {
            val database = DateDatabase(getApplication()).dateDao()
            database.insert(DateModel(startDate,endDate))
        }
    }

    fun getDate(dateId: Int, context:Context){
        launch {
            val database = DateDatabase(getApplication()).dateDao()
            val date = database.getDate(dateId = dateId)
            Log.i("dateListInfo",  SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date.startDate).toString())
            Log.i("dateListInfo",  SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date.endDate).toString())

        }
    }

    fun getAllDateData(){
        launch {
            val database = DateDatabase(getApplication()).dateDao()
            val allDate = database.getAllDates()
            for (date in allDate){
                Log.d("getAllDate",
                    "Id: " + date.uuid + " StartDate: " + date.startDate + "- EndDate: " + date.endDate )
            }
        }
    }

    fun deleteAll(context:Context){
        launch {
            val database = DateDatabase(getApplication()).dateDao()
            database.deleteAllDates()
        }
    }


}