package com.sus.calendar

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
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
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.sus.calendar.services.DataService
import java.util.Calendar

class StatisticFragment : Fragment() {

    private var uptHeight: Boolean = false
    private var uptWeight: Boolean = false
    private var uptBPM: Boolean = false
    private var uptSleep: Boolean = false
    private var uptAppetit: Boolean = false
    private var uptFellings: Boolean = false
    private var uptPressure: Boolean = false

    private var byear = 0
    private var ayear = 0
    private var bmonth = 0
    private var amonth = 0
    private var bday = 0
    private var aday = 0

    private var dataService: DataService? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.statistic_page, container, false)
        val avgHeight: BarChart = view.findViewById(R.id.averageHeight)
        val avgWeight: LineChart = view.findViewById(R.id.averageWeight)
        val avgCHSS: LineChart = view.findViewById(R.id.averageCHSS)
        val avgPressure: LineChart = view.findViewById(R.id.averagePressure)
        val avgSleep: LineChart = view.findViewById(R.id.averageSleep)
        /*setupBarChart(avgHeight, "Рост")
        setBarData(avgHeight)

        setupLineChart(avgWeight, "Вес")
        setLineData(avgWeight)

        setupLineChart(avgCHSS, "ЧСС")
        setLineData(avgCHSS)

        setupLineChart(avgSleep, "Сон")
        setLineData(avgSleep)*/


        var btnNext = view.findViewById<View>(R.id.buttonNext) as Button
        var avgHeightText = view.findViewById<TextView>(R.id.AverageValueHeight)
        var avgWeightText = view.findViewById<TextView>(R.id.AverageValueWeight)
        var avgbpmText = view.findViewById<TextView>(R.id.AverageValueCHSS)
        var avgSleepText = view.findViewById<TextView>(R.id.AverageValueSleep)
        var avgfeelingsText = view.findViewById<TextView>(R.id.AverageValueHealth)
        var avgPressureText = view.findViewById<TextView>(R.id.AverageValuePressure)
        var avgappetitText = view.findViewById<TextView>(R.id.AverageValueAppetite)
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
                    //update(ref, true)
                    val myBoolean = true
                    val cats = listOf(
                        "HEIGHT",
                        "WEIGHT",
                        "PULSE",
                        "PRESSURE",
                        "APPETITE",
                        "SLEEP",
                        "HEALTH"
                    )
                    var realnumber = ref
                    if (myBoolean) {
                        realnumber = if (ref == 6) 0 else ref + 1
                    }
                    Log.d("dada", "$realnumber $myBoolean")
                    when (realnumber) {
                        3 -> showinfoPressure(avgPressure)
                        4 -> showinfoCommon(avgfeelingsText, "HEALTH")
                        6 -> showinfoCommon(avgfeelingsText, "HEALTH")
                        0 -> showinfoBar(avgHeight, "Рост")
                        1 -> showinfoLine(avgWeight, "Вес")
                        2 -> showinfoLine(avgCHSS,"ЧСС")
                        5 -> showinfoLine(avgSleep, "Сон")

                    }
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
                    byear = year
                    bmonth = monthOfYear + 1
                    bday = dayOfMonth
                    try {
                        //update(simpleViewFlipper.displayedChild, false)
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
                    ayear = year
                    amonth = monthOfYear + 1
                    aday = dayOfMonth
                    try {
                        //update(simpleViewFlipper.displayedChild, false)
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

    private fun showinfoCommon(text: TextView, cat: String) {
        text.text = "norm"
    }

    private fun showinfoLine(line: LineChart, text: String) {
        setupLineChart(line, text)
        setLineData(line)
    }

    private fun showinfoBar(bar: BarChart, text: String) {
        setupBarChart(bar, text)
        setBarData(bar)
    }

    private fun showinfoPressure(pressure: LineChart) {
        val entriesUpperPressure = mutableListOf<Entry>()
        val entriesLowerPressure = mutableListOf<Entry>()
        for (i in 0 until 10) {
            entriesUpperPressure.add(Entry(i.toFloat(), (Math.random() * 20 + 110).toFloat()))
            entriesLowerPressure.add(Entry(i.toFloat(), (Math.random() * 10 + 70).toFloat()))
        }
        val dataSetUpperPressure = LineDataSet(entriesUpperPressure, "Верхнее давление")
        dataSetUpperPressure.color = Color.BLUE
        val dataSetLowerPressure = LineDataSet(entriesLowerPressure, "Нижнее давление")
        dataSetLowerPressure.color = Color.RED
        val lineData = LineData(dataSetUpperPressure, dataSetLowerPressure)
        pressure.data = lineData
        val description = Description()
        description.text = "Артериальное давление"
        pressure.description = description
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
            else ->{}
        }
    }

    private fun getbool(value: Int): Boolean {
        return when (value){
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

    /*private fun update(page: Int, next: Boolean) {
        val cats = listOf(
            "HEIGHT",
            "WEIGHT",
            "PULSE",
            "PRESSURE",
            "APPETITE",
            "SLEEP",
            "HEALTH"
        )
        var realnumber = page
        if (next) {
            realnumber = if (page == 6) 0 else page + 1
        }
        Log.d("dada", "$realnumber $next")
        when (realnumber) {
            3 -> showinfoPressure(avgPressureText, lineChartPressure, "PRESSURE")
            4 -> showinfoCommon(avgappetitText, cats[realnumber])
            6 -> showinfoCommon(avgfeelingsText, cats[realnumber])
            0 -> showinfoFloats(avgHeightText, barChartHeight, cats[realnumber], "Рост")
            1 -> showinfoFloats(avgWeightText, barChartWeight, cats[realnumber], "Вес")
            2 -> showinfoFloats(avgbpmText, barChartCHSS, cats[realnumber], "ЧСС")
            5 -> showinfoFloats(avgSleepText, barChartCHSS, cats[realnumber], "Сон")
        }
    }*/
}