package com.sus.calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.sus.calendar.entities.DateWithNotes
import com.sus.calendar.services.DataService
import java.time.LocalDate
import java.util.Calendar

class StatisticFragment : Fragment() {


    data class HealthRecord(
        val date: LocalDate,
        val height: Int, // Рост в см
        val weight: Double, // Вес в кг
        val heartRate: Int, // ЧСС
        val bloodPressure: String, // Давление (например, "120/80")
        val sleepHours: Double, // Сон в часах
        val wellbeing: Wellbeing, // Самочувствие
        val appetite: Appetite // Аппетит
    )

    // Перечисления для самочувствия и аппетита
    enum class Wellbeing {
        GOOD, BAD, NORMAL
    }

    enum class Appetite {
        GOOD, BAD, NO_APPETITE, NORMAL
    }

    enum class DataType {
        HEIGHT, WEIGHT, HEART_RATE, SLEEP_HOURS, WELLBEING, APPETITE
    }

    val healthRecords = listOf(
        HealthRecord(
            date = LocalDate.of(2024, 5, 17),
            height = 175,
            weight = 70.5,
            heartRate = 72,
            bloodPressure = "120/80",
            sleepHours = 7.5,
            wellbeing = Wellbeing.GOOD,
            appetite = Appetite.GOOD
        ),
        HealthRecord(
            date = LocalDate.of(2024, 5, 18),
            height = 175,
            weight = 70.0,
            heartRate = 70,
            bloodPressure = "118/78",
            sleepHours = 8.0,
            wellbeing = Wellbeing.NORMAL,
            appetite = Appetite.NORMAL
        ),
        HealthRecord(
            date = LocalDate.of(2024, 5, 19),
            height = 176,
            weight = 71.0,
            heartRate = 68,
            bloodPressure = "118/80",
            sleepHours = 10.0,
            wellbeing = Wellbeing.NORMAL,
            appetite = Appetite.NORMAL
        ),
        HealthRecord(
            date = LocalDate.of(2024, 5, 20),
            height = 174,
            weight = 69.0,
            heartRate = 780,
            bloodPressure = "140/70",
            sleepHours = 5.0,
            wellbeing = Wellbeing.BAD,
            appetite = Appetite.NO_APPETITE
        ),
        HealthRecord(
            date = LocalDate.of(2024, 5, 21),
            height = 175,
            weight = 67.0,
            heartRate = 69,
            bloodPressure = "120/80",
            sleepHours = 12.0,
            wellbeing = Wellbeing.NORMAL,
            appetite = Appetite.NORMAL
        ),
    )

    private var uptHeight: Boolean = false
    private var uptWeight: Boolean = false
    private var uptBPM: Boolean = false
    private var uptSleep: Boolean = false
    private var uptAppetit: Boolean = false
    private var uptFellings: Boolean = false
    private var uptPressure: Boolean = false

    private val handler = Handler(Looper.getMainLooper())
    private val dataService = this.context?.let { DataService.initial(it) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        val view = inflater.inflate(R.layout.statistic_page, container, false)
        val avgHeight: BarChart = view.findViewById(R.id.averageHeight)
        val avgWeight: LineChart = view.findViewById(R.id.averageWeight)
        val avgCHSS: LineChart = view.findViewById(R.id.averageCHSS)
        val avgPressure: LineChart = view.findViewById(R.id.averagePressure)
        val avgSleep: LineChart = view.findViewById(R.id.averageSleep)




        var btnNext = view.findViewById<View>(R.id.buttonNext) as Button
        var avgHeightText = view.findViewById<TextView>(R.id.AverageValueHeight)
        var avgWeightText = view.findViewById<TextView>(R.id.AverageValueWeight)
        var avgCHSSText = view.findViewById<TextView>(R.id.AverageValueCHSS)
        var avgSleepText = view.findViewById<TextView>(R.id.AverageValueSleep)
        var avgFeelingsText = view.findViewById<TextView>(R.id.AverageValueHealth)
        var avgPressureText = view.findViewById<TextView>(R.id.AverageValuePressure)
        var avgAppetitText = view.findViewById<TextView>(R.id.AverageValueAppetite)
        var simpleViewFlipper = view.findViewById<ViewFlipper>(R.id.simpleViewFlipper)

        var dateFrom = view.findViewById<EditText>(R.id.dateOt)
        var dateTo = view.findViewById<EditText>(R.id.dateDo)

        btnNext.setOnClickListener {
            Log.d("d", "${simpleViewFlipper.displayedChild}")

            var ref = simpleViewFlipper.displayedChild
            if (simpleViewFlipper.displayedChild == 6) {
                ref = 0
            }
            try {
                if (getbool(ref + 1)) {
                    update(ref, true, avgPressure, avgAppetitText, avgFeelingsText, avgHeight,
                        avgWeight, avgCHSS, avgSleep, avgHeightText, avgWeightText, avgCHSSText,
                        avgSleepText, avgPressureText, dateFrom, dateTo)
                }
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            chooseBool(ref)
            simpleViewFlipper.showNext()
        }

        dateFrom.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialogFrom = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val dateString = "$dayOfMonth-${monthOfYear + 1}-$year"
                    dateFrom.setText(dateString)
                    try {
                        update(simpleViewFlipper.displayedChild, false, avgPressure, avgAppetitText,
                            avgFeelingsText, avgHeight, avgWeight, avgCHSS, avgSleep, avgHeightText,
                            avgWeightText, avgCHSSText, avgSleepText, avgPressureText, dateFrom, dateTo)
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                    uptFellings = true
                    uptAppetit = true
                    uptBPM = true
                    uptHeight = true
                    uptSleep = true
                    uptWeight = true
                    uptPressure = true
                    chooseBool(simpleViewFlipper.displayedChild)
                },
                year, month, day
            )
            datePickerDialogFrom.show()
        }

        dateTo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val datePickerDialogTo = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val dateString = "$dayOfMonth-${monthOfYear + 1}-$year"
                    dateTo.setText(dateString)
                    try {
                        update(simpleViewFlipper.displayedChild, false, avgPressure, avgAppetitText,
                            avgFeelingsText, avgHeight, avgWeight, avgCHSS, avgSleep, avgHeightText,
                            avgWeightText, avgCHSSText, avgSleepText, avgPressureText, dateFrom, dateTo)
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                    uptFellings = true
                    uptAppetit = true
                    uptBPM = true
                    uptHeight = true
                    uptSleep = true
                    uptWeight = true
                    uptPressure = true
                    chooseBool(simpleViewFlipper.displayedChild)
                },
                year, month, day
            )
            datePickerDialogTo.show()
        }
        return view
    }


    private fun setupLineChart(chart: LineChart, text: String) {
        chart.description.isEnabled = true
        chart.description.text = text

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setLineData(chart: LineChart) {
        val entries = mutableListOf<Entry>()
        for (i in 0 until 10) {
            entries.add(Entry(i.toFloat(), (Math.random() * 20 + 110).toFloat()))
        }

        val dataSet = LineDataSet(entries, "Артериальное давление")
        dataSet.color = resources.getColor(R.color.green, null)
        dataSet.setCircleColor(resources.getColor(R.color.red, null))
        dataSet.valueTextSize = 10f
        dataSet.setDrawValues(false)

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)
        chart.data = lineData
        chart.invalidate()
    }

    private fun setupBarChart(chart: BarChart, text: String) {
        chart.description.isEnabled = true
        chart.description.text = text

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }

    private fun setBarData(chart: BarChart) {
        val entries = mutableListOf<BarEntry>()
        for (i in 0 until 10) {
            entries.add(BarEntry(i.toFloat(), (Math.random() * 20 + 110).toFloat()))
        }

        val dataSet = BarDataSet(entries, "Артериальное давление")
        dataSet.color = resources.getColor(R.color.green, null)
        dataSet.valueTextSize = 10f
        dataSet.setDrawValues(false)

        val dataSets = mutableListOf<IBarDataSet>()
        dataSets.add(dataSet)

        val barData = BarData(dataSets)
        chart.data = barData
        chart.invalidate()
    }

    fun findMostFrequent(list: List<String>): String? {
        return list.groupBy { it }
            .maxByOrNull { it.value.size }
            ?.key
    }

    @Throws(InterruptedException::class)
    private fun showinfoCommon(text: TextView, cat: String, aday: Int, amonth: Int, ayear: Int,
                               bday: Int, bmonth: Int, byear: Int) {
        val dateWithNotes: Array<List<DateWithNotes>?> = arrayOfNulls(1)

        val runnable = Runnable {
            dateWithNotes[0] = dataService!!.getMediumByDateandtype(cat, byear, ayear, bmonth, amonth, bday, aday) as List<DateWithNotes>?
        }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        text.text = dateWithNotes[0].toString()
    }

    private fun calculateAverages(
        records: List<HealthRecord>,
        startDate: LocalDate,
        endDate: LocalDate,
        dataType: DataType
    ) {
        // Фильтрация записей по заданному периоду
        val filteredRecords = records.filter { it.date in startDate..endDate }

        if (filteredRecords.isEmpty()) {
            println("Записей в указанном периоде нет.")
            return
        }

        when (dataType) {
            DataType.HEIGHT -> {
                val averageHeight = filteredRecords.map { it.height }.average()
                println("Средний рост: %.2f см".format(averageHeight))
            }
            DataType.WEIGHT -> {
                val averageWeight = filteredRecords.map { it.weight }.average()
                println("Средний вес: %.2f кг".format(averageWeight))
            }
            DataType.HEART_RATE -> {
                val averageHeartRate = filteredRecords.map { it.heartRate }.average()
                println("Средняя ЧСС: %.2f уд/мин".format(averageHeartRate))
            }
            DataType.SLEEP_HOURS -> {
                val averageSleepHours = filteredRecords.map { it.sleepHours }.average()
                println("Среднее количество сна: %.2f часов".format(averageSleepHours))
            }
            DataType.WELLBEING -> {
                val mostCommonWellbeing = filteredRecords.groupBy { it.wellbeing }
                    .maxByOrNull { it.value.size }?.key ?: Wellbeing.NORMAL
                println("Чаще всего встречаемое самочувствие: $mostCommonWellbeing")
            }
            DataType.APPETITE -> {
                val mostCommonAppetite = filteredRecords.groupBy { it.appetite }
                    .maxByOrNull { it.value.size }?.key ?: Appetite.NO_APPETITE
                println("Чаще всего встречаемый аппетит: $mostCommonAppetite")
            }
        }
    }

    private fun showinfoLine(avgtex: TextView, line: LineChart, cat: String, text: String, aday: Int,
                             amonth: Int, ayear: Int, bday: Int, bmonth: Int, byear: Int) {
        val vals: List<Float> = java.util.ArrayList()
        val avg = FloatArray(1)
        val runnable = Runnable {
            avg[0] =
                dataService!!.getMediumByDateandtype(cat, byear, ayear, bmonth, amonth, bday, aday)
            vals.addAll(
                dataService!!.getNotesByDateAndTypeF(cat, byear, ayear, bmonth, amonth, bday, aday
                )
            )
        }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        avgtex.text = avg[0].toString()

        setupLineChart(line, text)
        setLineData(line)
    }

    private fun showinfoBar(avgtex: TextView, bar: BarChart, cat: String, text: String, aday: Int,
                            amonth: Int, ayear: Int, bday: Int, bmonth: Int, byear: Int) {
        val vals: List<Float> = java.util.ArrayList()
        val avg = FloatArray(1)
        val runnable = Runnable {
            avg[0] =
                dataService!!.getMediumByDateandtype(cat, byear, ayear, bmonth, amonth, bday, aday)
            vals.addAll(
                dataService!!.getNotesByDateAndTypeF(cat, byear, ayear, bmonth, amonth, bday, aday
                )
            )
        }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        avgtex.text = avg[0].toString()

        setupBarChart(bar, text)
        setBarData(bar)
    }

    private fun showinfoPressure(avgPressureText:TextView, pressure: LineChart, aday: Int,
                                 amonth: Int, ayear: Int, bday: Int, bmonth: Int, byear: Int) {
       val vals: MutableList<PressureValue> = java.util.ArrayList<PressureValue>()
        val runnable = Runnable {
            vals.addAll(
                dataService!!.getPressureByDate(
                    byear,
                    ayear,
                    bmonth,
                    amonth,
                    bday,
                    aday
                )
            )
        }
        val thread = Thread(runnable)
        thread.start()
        thread.join()
        val entriesUpperPressure: MutableList<Entry> = java.util.ArrayList()
        val entriesLowerPressure: MutableList<Entry> = java.util.ArrayList()
        var highsSum = 0
        var lowsSum = 0
        for (i in vals.indices) {
            entriesUpperPressure.add(Entry(i.toFloat(), vals[i].high))
            entriesLowerPressure.add(Entry(i.toFloat(), vals[i].low))
            highsSum += vals[i].high
            lowsSum += vals[i].low
        }
        val reshigh = highsSum / vals.size
        val reslow = lowsSum / vals.size
        val vas = "$reshigh/$reslow"
        avgPressureText.text = vas
        val dataSetUpperPressure: LineDataSet = createLineDataSet(
            entriesUpperPressure,
            "Верхнее давление",
            resources.getColor(R.color.colorUpperPressure)
        )
        val dataSetLowerPressure: LineDataSet = createLineDataSet(
            entriesLowerPressure,
            "Нижнее давление",
            resources.getColor(R.color.colorLowerPressure)
        )

        val lineData = LineData(dataSetUpperPressure, dataSetLowerPressure)
        setupChart(pressure)
        pressure.setData(lineData)
    }


    private fun chooseBool(value: Int) {
        when (value) {
            0 -> uptHeight = false
            1 -> uptWeight = false
            2 -> uptBPM = false
            3 -> uptPressure = false
            4 -> uptAppetit = false
            5 -> uptSleep = false
            6 -> uptFellings = false
            else -> {}
        }
    }

    private fun getbool(value: Int): Boolean {
        return when (value) {
            0 -> uptHeight
            1 -> uptWeight
            2 -> uptBPM
            3 -> uptPressure
            4 -> uptAppetit
            5 -> uptSleep
            6 -> {
                uptFellings = false
                false
            }

            else -> false
        }
    }

    @Throws(InterruptedException::class)
    private fun update(page: Int, next: Boolean, avgPressure: LineChart, avgAppetitText: TextView,
                       avgFeelingsText: TextView, avgHeight: BarChart, avgWeight: LineChart,
                       avgCHSS: LineChart, avgSleep: LineChart, avgHeightText: TextView,
                       avgWeightText: TextView, avgCHSSText: TextView, avgSleepText: TextView,
                       avgPressureText: TextView, dateFrom: EditText, dateTo: EditText) {
        val parts1 = dateFrom.text.toString().split("-")
        val parts2 = dateTo.text.toString().split("-")
        val cats: List<String> = ArrayList(
            mutableListOf(
                "HEIGHT",
                "WEIGHT",
                "PULSE",
                "PRESSURE",
                "APPETITE",
                "SLEEP",
                "HEALTH"
            )
        )
        var realnumber = page
        if (next) {
            if (page == 6) {
                realnumber = 0
            } else {
                realnumber++
            }
        }
        Log.d("dada", "$realnumber $next")
        if (realnumber == 3) {
            showinfoPressure(avgPressureText, avgPressure,
                parts1[0].toIntOrNull() ?: 0, parts1[1].toIntOrNull() ?: 0,
                parts1[2].toIntOrNull() ?: 0, parts2[0].toIntOrNull() ?: 0,
                parts2[1].toIntOrNull() ?: 0, parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 4) {
            showinfoCommon(
                avgAppetitText, cats[realnumber], parts1[0].toIntOrNull() ?: 0,
                parts1[1].toIntOrNull() ?: 0, parts1[2].toIntOrNull() ?: 0,
                parts2[0].toIntOrNull() ?: 0, parts2[1].toIntOrNull() ?: 0,
                parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 6) {
            showinfoCommon(
                avgFeelingsText, cats[realnumber], parts1[0].toIntOrNull() ?: 0,
                parts1[1].toIntOrNull() ?: 0, parts1[2].toIntOrNull() ?: 0,
                parts2[0].toIntOrNull() ?: 0, parts2[1].toIntOrNull() ?: 0,
                parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 0) {
            showinfoBar(avgHeightText, avgHeight,  cats[realnumber], "Рост",
                parts1[0].toIntOrNull() ?: 0, parts1[1].toIntOrNull() ?: 0,
                parts1[2].toIntOrNull() ?: 0, parts2[0].toIntOrNull() ?: 0,
                parts2[1].toIntOrNull() ?: 0, parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 1) {
            showinfoLine(avgWeightText, avgWeight, cats[realnumber], "Вес",
                parts1[0].toIntOrNull() ?: 0, parts1[1].toIntOrNull() ?: 0,
                parts1[2].toIntOrNull() ?: 0, parts2[0].toIntOrNull() ?: 0,
                parts2[1].toIntOrNull() ?: 0, parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 2) {
            showinfoLine(avgCHSSText, avgCHSS, cats[realnumber], "ЧСС",
                parts1[0].toIntOrNull() ?: 0, parts1[1].toIntOrNull() ?: 0,
                parts1[2].toIntOrNull() ?: 0, parts2[0].toIntOrNull() ?: 0,
                parts2[1].toIntOrNull() ?: 0, parts2[2].toIntOrNull() ?: 0
            )
        } else if (realnumber == 5) {
            showinfoLine(avgSleepText, avgSleep, cats[realnumber], "Сон",
                parts1[0].toIntOrNull() ?: 0, parts1[1].toIntOrNull() ?: 0,
                parts1[2].toIntOrNull() ?: 0, parts2[0].toIntOrNull() ?: 0,
                parts2[1].toIntOrNull() ?: 0, parts2[2].toIntOrNull() ?: 0
            )
        }*/
    return view}
}