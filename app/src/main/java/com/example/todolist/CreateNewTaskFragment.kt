package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.todolist.adapter.CalendarAdapter
import com.example.todolist.adapter.CalendarToAdapter
import com.example.todolist.data.model.CalendarDateModel
import com.example.todolist.data.model.Month
import com.example.todolist.viewmodel.CreateNewTaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CreateNewTaskFragment : Fragment(), CalendarAdapter.onItemClickListener,CalendarToAdapter.onItemClickListener {

    private lateinit var  viewModel: CreateNewTaskViewModel
    private lateinit var calendarAfterRecyclerView: RecyclerView
    private lateinit var calendarBeforeRecyclerView: RecyclerView
    private lateinit var tvDateMonth: TextView


    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private val dates1 = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private lateinit var adapterTo: CalendarToAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private val calendarList1 = ArrayList<CalendarDateModel>()

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =  ViewModelProvider(this).get(CreateNewTaskViewModel::class.java)
        tvDateMonth = view.findViewById(R.id.date_tv)
        calendarBeforeRecyclerView = view.findViewById(R.id.calendarBefore_rv)
        calendarAfterRecyclerView = view.findViewById(R.id.calendarAfter_rv)
        button = view.findViewById(R.id.start_btn)



        setUpAdapter()
        setUpCalendar()
        setUpAfterAdapter()
        setUpAfterCalendar()

        getNowDate()
        observeLiveData()
        //viewModel.getDate(2,requireContext())
        //EndDate(30 Nisan) > StartDate (5 Nisan) olursa veriyi kaydetmeli!!
        button.setOnClickListener {
            viewModel.processSelectedDates(viewModel.startDate.value!!,viewModel.endDate.value!!,requireContext())
            viewModel.getAllDateData()
        }
    }


    //Select Start Day
    override fun onStartItemClick(list: CalendarDateModel) {
        viewModel.setStartDate(list.data)
    }
    //Select End Day
    override fun onEndItemClick(list: CalendarDateModel) {
        viewModel.setEndDate(list.data)
    }

    private fun getNowDate(){
        val cal = Calendar.getInstance()

        val months = listOf(
            Month("January", 1),
            Month("February", 2),
            Month("March", 3),
            Month("April", 4),
            Month("May", 5),
            Month("June", 6),
            Month("July", 7),
            Month("August", 8),
            Month("September", 9),
            Month("October", 10),
            Month("November", 11),
            Month("December", 12)
        )

        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val monthName = months.get(month).name
        val year = cal.get(Calendar.YEAR)
        tvDateMonth.text = "$dayOfMonth"+"th" +" $monthName,$year"
    }

    private fun observeLiveData(){
        viewModel.startDate.observe(viewLifecycleOwner, Observer {
            if(it == null){
                Toast.makeText(requireContext(), "Please Fill Start Date", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "StartDate:" + it.time, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.endDate.observe(viewLifecycleOwner, Observer {
            if(it == null){
                Toast.makeText(requireContext(), "Please Fill End Date", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "endDate:" + it.time, Toast.LENGTH_SHORT).show()
            }
        })
    }
    /**
     * Setting up adapter for recyclerview
     */
    private fun setUpAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendarBeforeRecyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapter.setData(calendarList2)
            adapter.setOnStartItemClickListener(this)
        }
        calendarBeforeRecyclerView.adapter = adapter
    }

    private fun setUpAfterAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendarAfterRecyclerView)
        adapterTo = CalendarToAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList1.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapterTo.setData(calendarList1)
            adapterTo.setOnEndItemClickListener(this)
        }
        calendarAfterRecyclerView.adapter = adapterTo
    }

    /**
     * Function to setup calendar for every month
     */
    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        tvDateMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setOnStartItemClickListener(this)
        adapter.setData(calendarList)
    }

    private fun setUpAfterCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        tvDateMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates1.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates1.size < maxDaysInMonth) {
            dates1.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList1.clear()
        calendarList1.addAll(calendarList)
        adapterTo.setOnEndItemClickListener(this)
        adapterTo.setData(calendarList)
    }




}