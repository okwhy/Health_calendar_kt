package com.sus.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.DateWithNotes
import com.sus.calendar.entities.Note
import com.sus.calendar.services.DataService
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Arrays
import java.util.Calendar

class CalendarFragment : Fragment() {
    private lateinit var curdate: LocalDate
    private var calendar: Calendar? = null
    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private var dataService: DataService? = null
    private var texts: List<EditText>? = null
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.calendar_page, container, false)
        calendar = Calendar.getInstance()
        curdate = LocalDateTime.now().toLocalDate()
        currentDay = curdate.getDayOfMonth()
        currentMonth = curdate.getMonthValue()
        currentYear = curdate.getYear()
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        //Настройка календаря:
        calendarView.setSwipeEnabled(false)


        //------------------------------------------------------
        dataService = this.context?.let { DataService.initial(it) }
        try {
            addmarks(calendarView)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        val calendarStrings: List<String> = ArrayList()
        val days = IntArray(31)
        val months = IntArray(12)
        val years = IntArray(10)
        val textInputHeight = view.findViewById<EditText>(R.id.textInputHeight)
        val textInputWeight = view.findViewById<EditText>(R.id.textInputWeight)
        val textInputPulse = view.findViewById<EditText>(R.id.textInputPulse)
        val textInputPressure = view.findViewById<EditText>(R.id.textInputPressure)
        val textInputAppetite = view.findViewById<EditText>(R.id.textInputAppetite)
        val textInputSlepping = view.findViewById<EditText>(R.id.textInputSlepping)
        val textInputHealth = view.findViewById<EditText>(R.id.textInputHealth)
        texts = ArrayList(
            Arrays.asList(
                textInputHeight,
                textInputWeight,
                textInputPulse,
                textInputPressure,
                textInputAppetite,
                textInputSlepping,
                textInputHealth
            )
        )
        val dayInfo = view.findViewById<View>(R.id.dayInfo)
        val dayHeight = view.findViewById<View>(R.id.dayHeight)
        val dayWeight = view.findViewById<View>(R.id.dayWeight)
        val dayPulse = view.findViewById<View>(R.id.dayPulse)
        val dayPressure = view.findViewById<View>(R.id.dayPressure)
        val dayAppetite = view.findViewById<View>(R.id.dayAppetite)
        val daySlepping = view.findViewById<View>(R.id.daySlepping)
        val dayHealth = view.findViewById<View>(R.id.dayHealth)
        var notes: Map<String?, String?>? = null
        notes = try {
            fetchDate(currentYear, currentMonth, currentDay)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        checkdate(currentYear, currentMonth, currentDay)
        if (!(notes == null || notes.isEmpty())) {
            textInputHeight.setText(if (notes["HEIGHT"] == null) "Нет данных" else notes["HEIGHT"])
            textInputWeight.setText(if (notes["WEIGHT"] == null) "Нет данных" else notes["WEIGHT"])
            textInputPulse.setText(if (notes["PULSE"] == null) "Нет данных" else notes["PULSE"])
            textInputPressure.setText(if (notes["PRESSURE"] == null) "Нет данных" else notes["PRESSURE"]) // Артем
            textInputAppetite.setText(if (notes["APPETITE"] == null) "Нет данных" else notes["APPETITE"])
            textInputSlepping.setText(if (notes["SLEEP"] == null) "Нет данных" else notes["SLEEP"])
            textInputHealth.setText(if (notes["HEALTH"] == null) "Нет данных" else notes["HEALTH"])
        } else {
            textInputHeight.setText("Нет данных")
            textInputWeight.setText("Нет данных")
            textInputPulse.setText("Нет данных")
            textInputPressure.setText("Нет данных")
            textInputAppetite.setText("Нет данных")
            textInputSlepping.setText("Нет данных")
            textInputHealth.setText("Нет данных")
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
                if (dayInfo.visibility == View.INVISIBLE) {
                    dayInfo.visibility = View.VISIBLE
                    dayHeight.visibility = View.VISIBLE
                    dayWeight.visibility = View.VISIBLE
                    dayPulse.visibility = View.VISIBLE
                    dayPressure.visibility = View.VISIBLE
                    dayAppetite.visibility = View.VISIBLE
                    daySlepping.visibility = View.VISIBLE
                    dayHealth.visibility = View.VISIBLE
                }
                for (i in 0..30) {
                    if (days[i] == currentDay) {
                        for (j in 0..11) {
                            if (months[j] == currentMonth) {
                                for (k in 0..9) {
                                    if (years[k] == currentYear) {
                                        textInputHeight.setText(calendarStrings[i])
                                        textInputWeight.setText(calendarStrings[i + 1])
                                        textInputPulse.setText(calendarStrings[i + 2])
                                        textInputPressure.setText(calendarStrings[i + 3])
                                        textInputAppetite.setText(calendarStrings[i + 4])
                                        textInputSlepping.setText(calendarStrings[i + 5])
                                        textInputHealth.setText(calendarStrings[i + 6])
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
                var notes: Map<String?, String?>? = null
                notes = try {
                    fetchDate(currentYear, currentMonth, currentDay) // все записи за текущий день
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
                checkdate(currentYear, currentMonth, currentDay)
                if (!(notes == null || notes!!.isEmpty())) {
                    textInputHeight.setText(if (notes!!["HEIGHT"] == null) "Нет данных" else notes!!["HEIGHT"])
                    textInputWeight.setText(if (notes!!["WEIGHT"] == null) "Нет данных" else notes!!["WEIGHT"])
                    textInputPulse.setText(if (notes!!["PULSE"] == null) "Нет данных" else notes!!["PULSE"])
                    textInputPressure.setText(if (notes!!["PRESSURE"] == null) "Нет данных" else notes!!["PRESSURE"])
                    textInputAppetite.setText(if (notes!!["APPETITE"] == null) "Нет данных" else notes!!["APPETITE"])
                    textInputSlepping.setText(if (notes!!["SLEEP"] == null) "Нет данных" else notes!!["SLEEP"])
                    textInputHealth.setText(if (notes!!["HEALTH"] == null) "Нет данных" else notes!!["HEALTH"])
                } else {
                    textInputHeight.setText("Нет данных")
                    textInputWeight.setText("Нет данных")
                    textInputPulse.setText("Нет данных")
                    textInputPressure.setText("Нет данных")
                    textInputAppetite.setText("Нет данных")
                    textInputSlepping.setText("Нет данных")
                    textInputHealth.setText("Нет данных")
                }
            }
        })
        calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                try {
                    addmarks(calendarView)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        })
        calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                try {
                    addmarks(calendarView)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        })
        val stateOfAppetite = arrayOf<String?>(
            "Введите аппетит",
            "Отличный",
            "Хороший",
            "Средний",
            "Плохой",
            "Нет аппетита"
        )
        val spinnerAppetite = view.findViewById<Spinner>(R.id.textInputAppetite_Spiner)
        val appetiteAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, stateOfAppetite)
        appetiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAppetite.adapter = appetiteAdapter
        val stateOfHealth = arrayOf<String?>(
            "Введите самочувствие",
            "Отличное",
            "Хорошее",
            "Среднее",
            "Плохое",
            "Ужасное"
        )
        val spinnerHealth = view.findViewById<Spinner>(R.id.textInputHealth_Spiner)
        val appetiteHealth: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, stateOfHealth)
        appetiteHealth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHealth.adapter = appetiteHealth
        val saveTextButton = view.findViewById<Button>(R.id.saveTextButton_1)
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
            if (textInputAppetite.text.toString() != "Нет данных") {
                notes1["APPETITE"] = textInputAppetite.text.toString()
            }
            if (textInputSlepping.text.toString() != "Нет данных") {
                notes1["SLEEP"] = textInputSlepping.text.toString()
            }
            if (textInputHealth.text.toString() != "Нет данных") {
                notes1["HEALTH"] = textInputHealth.text.toString()
            }
            Log.d("dafa", "$notes1 $currentYear")
            insertorupdateDate(notes1, currentYear, currentMonth, currentDay)
        }

        // Подключаемся к кнопке в макете вашей активности
        val showPopupButton = view.findViewById<Button>(R.id.showPopupButton)

        // Устанавливаем слушателя нажатия на кнопку
        showPopupButton.setOnClickListener { showPopup() }
        return view
    }

    private fun showPopup() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_layout, null)
        val popupText = popupView.findViewById<TextView>(R.id.popupText)
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)
        popupText.text = """
            Приложение Health Calendar представляет собой инструмент для отслеживания основных показателей здоровья.
            
            На главном экране приложения расположен календарь, в котором можно установить, за какой день вносятся данные. Для сохранения введенных данных необходимо нажать на кнопку "Сохранить".
            
            Для удобства использования календаря в приложение были добавлены метки для дней. Дни, в которые данные не были своевременно введены помечаются красной полоской, а дни, в которые данные были внесены вовремя помечаются зеленой полоской.
            
            Приятного вам использования!
            """.trimIndent()
        closeButton.setOnClickListener { alertDialog!!.dismiss() }
        builder.setView(popupView)
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun insertorupdateDate(notes: Map<String, String>, year: Int, month: Int, date: Int) {
        val ref = arrayOfNulls<DateSQL>(1)
        val id = LongArray(1)
        val noteslist: MutableList<Note> = ArrayList()
        val runnable = Runnable {
            ref[0] = dataService!!.getDateNoNotes(year, month, date)
            if (ref[0] == null) {
                id[0] = dataService!!.insertDate(year, month, date)
            } else id[0] = ref[0]!!.id
            for (n in notes.keys) {
                noteslist.add(Note(n, notes[n], id[0]))
            }
            for (n in noteslist) {
                val a = dataService!!.insertOrUpdateNote(n)
            }
        }
        val thread = Thread(runnable)
        thread.start()
    }

    @Throws(InterruptedException::class)
    private fun fetchDate(year: Int, month: Int, date: Int): Map<String?, String?>? {
        val dateSQL = arrayOfNulls<DateWithNotes>(1)
        val runnable = Runnable { dateSQL[0] = dataService!!.getDate(year, month, date) }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        if (dateSQL[0] == null) {
            return null
        }
        val notes = dateSQL[0]!!.notes
        val notesRes: MutableMap<String?, String?> = HashMap()
        for (n in notes!!) {
            notesRes[n.type] = n.value
        }
        return notesRes
    }

    @Throws(InterruptedException::class)
    fun addmarks(calendarView: CalendarView) {
        val cal = calendarView.currentPageDate
        val year = cal[1]
        val month = cal[2] + 1
        val days_amount = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val day = cal[5]
        var noedit: Boolean
        var seldate: LocalDate
        val daysWithData: MutableList<Int> = ArrayList()
        val runnable = Runnable { daysWithData.addAll(dataService!!.getDaysByMonth(year, month)) }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        val events: MutableList<EventDay> = ArrayList()
        for (iter in 0 until days_amount) {
            val calendar_temp = Calendar.getInstance()
            seldate = LocalDate.of(year, month, day + iter)
            noedit = seldate.isAfter(curdate) || ChronoUnit.DAYS.between(curdate, seldate) < -3
            calendar_temp[year, month - 1] = day + iter
            if (noedit) {
                if (daysWithData.contains(day + iter)) {
                    events.add(EventDay(calendar_temp, R.drawable.ic_line2))
                } else {
                    events.add(EventDay(calendar_temp, R.drawable.ic_line))
                }
            }
            if (daysWithData.contains(day + iter)) {
                events.add(EventDay(calendar_temp, R.drawable.ic_line2))
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
    }
}