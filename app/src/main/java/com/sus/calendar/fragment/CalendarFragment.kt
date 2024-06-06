package com.sus.calendar.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.sus.calendar.MainActivity
import com.sus.calendar.R
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.CalendarPageBinding
import com.sus.calendar.dtos.DateWithIdNotesUidDto
import com.sus.calendar.dtos.tmpSolution.DataWithNotesTmp
import com.sus.calendar.dtos.tmpSolution.NoteTmp
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.services.ApiService
import com.sus.calendar.services.DataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Arrays
import java.util.Calendar
import java.util.UUID

class CalendarFragment : Fragment() {

    private val appetite_map = hashMapOf<Int,String>(
        2 to "GOOD",
        3 to "BAD",
        4 to "NORMAL",
        1 to "NO_APPETITE"
    )

    private val selffeelings_map = hashMapOf<Int,String>(

        1 to "GOOD",
        3 to "BAD",
        2 to "NORMAL"

    )


    private lateinit var spinnerAppetite:Spinner
    private lateinit var spinnerHealth:Spinner
    private lateinit var curdate: LocalDate
    private var calendar: Calendar? = null
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private var dataService: DataService? = null
    private var texts: List<EditText>? = null
    private lateinit var alertDialog: AlertDialog
    private lateinit var binding: CalendarPageBinding
    private lateinit var apiService: ApiService
    private lateinit var dayid:TextView
    private var data:MutableList<DateWithIdNotesUidDto> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apiService=RetrofitClient.instance
        binding = CalendarPageBinding.inflate(layoutInflater, container, false)
        calendar = Calendar.getInstance()
        curdate = LocalDateTime.now().toLocalDate()
        currentDay = curdate.getDayOfMonth()
        currentMonth = curdate.getMonthValue()
        currentYear = curdate.getYear()
        val calendarView = binding.calendarView

        calendarView.setSwipeEnabled(false)
        dataService = this.context?.let { DataService.initial(it) }
        loaddata(calendarView)
//        addmarks(calendarView)


        val textInputHeight = binding.textInputHeight
        val textInputWeight = binding.textInputWeight
        val textInputPulse = binding.textInputPulse
        val textInputPressure = binding.textInputPressure
        val textInputSlepping = binding.textInputSlepping
        dayid=binding.dayid
        spinnerAppetite = binding.textInputAppetiteSpiner
        spinnerHealth = binding.textInputHealthSpiner
        texts = ArrayList(
            Arrays.asList(
                textInputHeight,
                textInputWeight,
                textInputPulse,
                textInputPressure,
                textInputSlepping,
            )
        )
        var notes: Map<String?, String?>? =
            fetchDate(currentYear, currentMonth, currentDay)
        checkdate(currentYear, currentMonth, currentDay)
        if (!(notes == null || notes!!.isEmpty())) {
            textInputHeight.setText(if (notes!!["HEIGHT"] == null) "Нет данных" else notes!!["HEIGHT"])
            textInputWeight.setText(if (notes!!["WEIGHT"] == null) "Нет данных" else notes!!["WEIGHT"])
            textInputPulse.setText(if (notes!!["PULSE"] == null) "Нет " else notes!!["PULSE"])
            textInputPressure.setText(if (notes!!["PRESSURE"] == null) "Нет данных" else notes!!["PRESSURE"])
            textInputSlepping.setText(if (notes!!["SLEEP"] == null) "Нет данных" else notes!!["SLEEP"])
            dayid.text=notes["dateid"]
            when(notes!!["APPETITE"])
            {
                "NO_APPETITE"->spinnerAppetite.setSelection(1)
                "GOOD"->spinnerAppetite.setSelection(2)
                "BAD"->spinnerAppetite.setSelection(3)
                "NORMAL"->spinnerAppetite.setSelection(4)
                else->spinnerAppetite.setSelection(0)
            }

            when(notes!!["HEALTH"])
            {
                "GOOD"->spinnerHealth.setSelection(1)
                "BAD"->spinnerHealth.setSelection(3)
                "NORMAL"->spinnerHealth.setSelection(2)
                else->spinnerHealth.setSelection(0)
            }

        } else {
            textInputHeight.setText("Нет данных")
            textInputWeight.setText("Нет данных")
            textInputPulse.setText("Нет данных")
            textInputPressure.setText("Нет данных")
            spinnerAppetite.setSelection(0)
            textInputSlepping.setText("Нет данных")
            spinnerHealth.setSelection(0)
            dayid.text=""
        }

        calendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val cal = eventDay.calendar
                val year = cal[1]
                val month = cal[2]
                val dayOfMonth = cal[5]
                currentYear = year
                currentMonth = month + 1
                currentDay = dayOfMonth

                var currentdayNotes: Map<String?, String?>? = fetchDate(currentYear, currentMonth, currentDay) // все записи за текущий день

                checkdate(currentYear, currentMonth, currentDay)
                if (!(currentdayNotes == null || currentdayNotes!!.isEmpty())) {
                    textInputHeight.setText(if (currentdayNotes!!["HEIGHT"] == null) "Нет данных" else currentdayNotes!!["HEIGHT"])
                    textInputWeight.setText(if (currentdayNotes!!["WEIGHT"] == null) "Нет данных" else currentdayNotes!!["WEIGHT"])
                    textInputPulse.setText(if (currentdayNotes!!["PULSE"] == null) "Нет " else currentdayNotes!!["PULSE"])
                    textInputPressure.setText(if (currentdayNotes!!["PRESSURE"] == null) "Нет данных" else currentdayNotes!!["PRESSURE"])
                    textInputSlepping.setText(if (currentdayNotes!!["SLEEP"] == null) "Нет данных" else currentdayNotes!!["SLEEP"])
                    dayid.text=currentdayNotes["dateid"]
                    when(currentdayNotes["APPETITE"])
                    {
                        "NO_APPETITE"->spinnerAppetite.setSelection(1)
                        "GOOD"->spinnerAppetite.setSelection(2)
                        "BAD"->spinnerAppetite.setSelection(3)
                        "NORMAL"->spinnerAppetite.setSelection(4)
                        else->spinnerAppetite.setSelection(0)
                    }

                    when(currentdayNotes["HEALTH"])
                    {
                        "GOOD"->spinnerHealth.setSelection(1)
                        "BAD"->spinnerHealth.setSelection(3)
                        "NORMAL"->spinnerHealth.setSelection(2)
                        else->spinnerHealth.setSelection(0)
                    }

                } else {
                    textInputHeight.setText("Нет данных")
                    textInputWeight.setText("Нет данных")
                    textInputPulse.setText("Нет данных")
                    textInputPressure.setText("Нет данных")
                    spinnerAppetite.setSelection(0)
                    textInputSlepping.setText("Нет данных")
                    spinnerHealth.setSelection(0)
                    dayid.text=""
                }
            }
        })
        calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                    addmarks(calendarView)
            }
        })
        calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                    addmarks(calendarView)
            }
        })
        val stateOfAppetite = arrayOf<String?>(
            "Введите аппетит",
            "Нет аппетита",
            "Хороший",
            "Плохой",
            "Нормальный"
        )
        spinnerAppetite = binding.textInputAppetiteSpiner
        val appetiteAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                stateOfAppetite
            )
        appetiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAppetite.adapter = appetiteAdapter
        val stateOfHealth = arrayOf<String?>(
            "Введите самочувствие",
            "Хорошее",
            "Нормальное",
            "Плохое",
        )

        spinnerHealth = binding.textInputHealthSpiner
        val appetiteHealth: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                stateOfHealth
            )
        appetiteHealth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHealth.adapter = appetiteHealth
        val saveTextButton = binding.saveTextButton1
        saveTextButton.setOnClickListener { v: View? ->
            val notes1: MutableMap<String, String> = HashMap()
            if (textInputHeight.text.toString() != "Нет данных") {
                notes1["HEIGHT"] = textInputHeight.text.toString()
            }
            if (textInputWeight.text.toString() != "Нет данных") {
                notes1["WEIGHT"] = textInputWeight.text.toString()
            }
            if (textInputPulse.text.toString() != "Нет данных") {
                notes1["PULSE"] = textInputPulse.text.toString()
            }
            if (textInputPressure.text.toString() != "Нет данных") {
                notes1["PRESSURE"] = textInputPressure.text.toString()
            }
            if (spinnerAppetite.getItemAtPosition(spinnerAppetite.selectedItemPosition)
                    .toString() != "Введите аппетит"
            ) {
                notes1["APPETITE"] =selffeelings_map[spinnerHealth.selectedItemPosition].toString()
            }
            if (textInputSlepping.text.toString() != "Нет данных") {
                notes1["SLEEP"] = textInputSlepping.text.toString()
            }
            if (spinnerAppetite.getItemAtPosition(spinnerAppetite.selectedItemPosition)
                    .toString() != "Введите аппетит"
            ) {
                notes1["HEALTH"] =appetite_map[spinnerAppetite.selectedItemPosition].toString()

            }
            if(notes1.isNotEmpty()){
                insertorupdateDate(notes1, currentYear, currentMonth, currentDay,dayid.text.toString())
            }
        }

        val showPopupButton = binding.showPopupButton

        showPopupButton.setOnClickListener { showPopup() }
        return binding.root
    }

    private fun showPopup() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_layout, null)
        val popupText = popupView.findViewById<TextView>(R.id.popupText)
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)
        popupText.text = getString(R.string.health_calendar).trimIndent()
        closeButton.setOnClickListener { alertDialog!!.dismiss() }
        builder.setView(popupView)
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun insertorupdateDate(notes: Map<String, String>, year: Int, month: Int, date: Int,id:String) {
        var ref: DateSQL?
        val noteslist: MutableList<NoteTmp> = mutableListOf()
        if(id==""){
            for (n in notes.keys){
                noteslist.add(NoteTmp(n, notes[n]!!))
            }
            val uid= UUID.randomUUID().toString().replace("-", "").take(10)
            val tmp=DataWithNotesTmp(year,month,date, noteslist.toSet(),uid)
            val callAddDate=apiService.addDate(MainActivity.DataManager.getUserData()!!.id,tmp)
            callAddDate.enqueue(object :Callback<DateWithIdNotesUidDto>{
                override fun onResponse(
                    call: Call<DateWithIdNotesUidDto>,
                    response: Response<DateWithIdNotesUidDto>
                ) {
                    if(response.isSuccessful){
                        data.add(response.body()!!)
                        dayid.text= response.body()!!.uid
//                        dayid.visibility=View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<DateWithIdNotesUidDto>, t: Throwable) {
                    Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            }
            )
        }else{
            for (n in notes.keys){
                noteslist.add(NoteTmp(n, notes[n]!!))
            }
            val tmp=DataWithNotesTmp(year,month,date, noteslist.toSet(),id)
            val callUpdateDate=apiService.updateDate(MainActivity.DataManager.getUserData()!!.id,tmp)
            callUpdateDate.enqueue(object :Callback<DateWithIdNotesUidDto>{
                override fun onResponse(
                    call: Call<DateWithIdNotesUidDto>,
                    response: Response<DateWithIdNotesUidDto>
                ) {
                    if(response.isSuccessful){
                        val tmp2=response.body()
                        data.find { it.uid == tmp2!!.uid }?.let { existingItem ->
                            data[data.indexOf(existingItem)] = tmp2!!
                        }
                    }
                }

                override fun onFailure(call: Call<DateWithIdNotesUidDto>, t: Throwable) {
                    Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                ref = dataService!!.getDateNoNotes(year, month, date)
//                if (ref == null) {
//                    id = dataService!!.insertDate(year, month, date)
//                } else id = ref!!.id
//                for (n in notes.keys) {
//                    noteslist.add(Note(n, notes[n], id!!))
//                }
//                for (n in noteslist) {
//                    val a = dataService!!.insertOrUpdateNote(n)
//                }
//            }
//        }

    }

    private fun fetchDate(year: Int, month: Int, date: Int): Map<String?, String?>? {
        val dateSQL: DateWithIdNotesUidDto? = data.filter { it.year==year
                &&it.month==month
                &&it.day==date }.firstOrNull()


        if (dateSQL == null) {
            return null
        }
        val notes = dateSQL.notes
        val notesRes: MutableMap<String?, String?> = HashMap()
        for (n in notes) {
            notesRes[n.type] = n.value
        }
        notesRes["dateid"]=dateSQL.uid
        return notesRes
    }

    fun addmarks(calendarView: CalendarView) {
        val cal = calendarView.currentPageDate
        val year = cal[1]
        val month = cal[2] + 1
        val days_amount = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val day = cal[5]
        var noedit: Boolean
        var seldate: LocalDate
        val daysWithData: MutableList<Int> = data.filter { it.year==year && it.month==month }.map { it.day }.toMutableList()
        val events: MutableList<EventDay> = mutableListOf()
            for (iter in 0 until days_amount) {
                val calendartemp = Calendar.getInstance()
                seldate = LocalDate.of(year, month, day + iter)
                noedit = seldate.isAfter(curdate) || ChronoUnit.DAYS.between(curdate, seldate) < -3
                calendartemp[year, month - 1] = day + iter
                if (noedit) {
                    if (daysWithData.contains(day + iter)) {
                        events.add(EventDay(calendartemp, R.drawable.ic_line2))
                    } else {
                        events.add(EventDay(calendartemp, R.drawable.ic_line))
                    }
                }
                if (daysWithData.contains(day + iter)) {
                    events.add(EventDay(calendartemp, R.drawable.ic_line2))
                }
            }
            calendarView.setEvents(events)
    }

    private fun checkdate(year: Int, month: Int, date: Int) {
        val seldate = LocalDate.of(year, month, date)
        val noedit = seldate.isAfter(curdate) || ChronoUnit.DAYS.between(curdate, seldate) < -3
        for (t in texts!!) {
            t.isFocusable = !noedit
            t.isFocusableInTouchMode = !noedit
            t.isClickable = !noedit
            t.isLongClickable = !noedit
            t.isCursorVisible = !noedit
        }
        spinnerAppetite.isClickable=!noedit
        spinnerHealth.isClickable=!noedit
    }
    private fun loaddata(calendarView: CalendarView){
        val callGetDates=apiService.getUserDats(MainActivity.DataManager.getUserData()!!.id)
        callGetDates.enqueue(object : Callback<List<DateWithIdNotesUidDto>> {
            override fun onResponse(call: Call<List<DateWithIdNotesUidDto>>, response: Response<List<DateWithIdNotesUidDto>>) {
                if (response.isSuccessful) {
                    data.addAll(response.body()!!)
                    addmarks(calendarView)
                } else {
                    Toast.makeText(
                        context,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<List<DateWithIdNotesUidDto>>, t: Throwable) {
                Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}