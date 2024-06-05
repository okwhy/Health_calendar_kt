package com.sus.calendar.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.sus.calendar.databinding.FragmentStatisticMemberBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class StatisticMember : Fragment() {
    private lateinit var binding: FragmentStatisticMemberBinding

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
        GOOD, BAD, NORMAL, NOT_STATED
    }

    enum class Appetite {
        GOOD, BAD, NO_APPETITE, NORMAL, NOT_STATED
    }

    enum class DataType {
        HEIGHT, WEIGHT, HEART_RATE, SLEEP_HOURS, WELLBEING, APPETITE, PRESSURE
    }

    var healthRecords = mutableListOf<HealthRecord>()

    /*var healthRecords = listOf(
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
    )*/

    private var uptHeight: Boolean = false
    private var uptWeight: Boolean = false
    private var uptBPM: Boolean = false
    private var uptSleep: Boolean = false
    private var uptAppetit: Boolean = false
    private var uptFellings: Boolean = false
    private var uptPressure: Boolean = false

    private var boolDateFrom: Boolean = false
    private var boolDateTo: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticMemberBinding.inflate(inflater, container, false)
        val view = binding.root

        val avgHeight = binding.averageHeightMember
        val avgWeight = binding.averageWeightMember
        val avgCHSS = binding.averageCHSSMember
        val avgPressure = binding.averagePressureMember
        val avgSleep = binding.averageSleepMember
        val avgFellings = binding.averageHealthMember
        val avgAppetite = binding.averageAppetiteMember

        var btnNext = binding.buttonNextMember
        var avgHeightText = binding.AverageValueHeightMember
        var avgWeightText = binding.AverageValueWeightMember
        var avgCHSSText = binding.AverageValueCHSSMember
        var avgSleepText = binding.AverageValueSleepMember
        var avgFeelingsText = binding.AverageValueHealthMember
        var avgPressureText = binding.AverageValuePressureMember
        var avgAppetitText = binding.AverageValueAppetiteMember
        var simpleViewFlipper = binding.simpleViewFlipperMember

        var dateFrom = binding.dateOtMember
        var dateTo = binding.dateDoMember

        btnNext.setOnClickListener {
            Log.d("d", "${simpleViewFlipper.displayedChild}")
            val startDate = getDateFromEditText(dateFrom)
            val endDate = getDateFromEditText(dateTo)
            var ref = simpleViewFlipper.displayedChild
            if (simpleViewFlipper.displayedChild == 6) {
                ref = 0
                update(
                    -1, true, avgPressure, avgAppetitText, avgFeelingsText, avgHeight,
                    avgWeight, avgCHSS, avgSleep, avgHeightText, avgWeightText, avgCHSSText,
                    avgSleepText, avgPressureText, avgFellings, avgAppetite, startDate, endDate
                )
            }
            try {
                if (getbool(ref + 1)) {
                    update(
                        ref, true, avgPressure, avgAppetitText, avgFeelingsText, avgHeight,
                        avgWeight, avgCHSS, avgSleep, avgHeightText, avgWeightText, avgCHSSText,
                        avgSleepText, avgPressureText, avgFellings, avgAppetite, startDate, endDate
                    )
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
                    val dateString =
                        String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year)
                    dateFrom.setText(dateString)
                    boolDateFrom = true
                    if (boolDateFrom == true && boolDateTo == true) {
                        val startDate = getDateFromEditText(dateFrom)
                        val endDate = getDateFromEditText(dateTo)
                        update(
                            -1,
                            true,
                            avgPressure,
                            avgAppetitText,
                            avgFeelingsText,
                            avgHeight,
                            avgWeight,
                            avgCHSS,
                            avgSleep,
                            avgHeightText,
                            avgWeightText,
                            avgCHSSText,
                            avgSleepText,
                            avgPressureText,
                            avgFellings,
                            avgAppetite,
                            startDate,
                            endDate
                        )
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
                    val dateString =
                        String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year)
                    dateTo.setText(dateString)
                    boolDateTo = true
                    if (boolDateFrom == true && boolDateTo == true) {
                        val startDate = getDateFromEditText(dateFrom)
                        val endDate = getDateFromEditText(dateTo)
                        update(
                            -1,
                            true,
                            avgPressure,
                            avgAppetitText,
                            avgFeelingsText,
                            avgHeight,
                            avgWeight,
                            avgCHSS,
                            avgSleep,
                            avgHeightText,
                            avgWeightText,
                            avgCHSSText,
                            avgSleepText,
                            avgPressureText,
                            avgFellings,
                            avgAppetite,
                            startDate,
                            endDate
                        )
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

    fun createHealthRecordFromString(data: String): HealthRecord {
        val parts = data.split(",")

        val date = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE)
        val height = parts[1].trim().toInt()
        val weight = parts[2].trim().toDouble()
        val heartRate = parts[3].trim().toInt()
        val bloodPressure = parts[4].trim()
        val sleepHours = parts[5].trim().toDouble()
        val wellbeing = Wellbeing.valueOf(parts[6].trim().uppercase(Locale.getDefault()))
        val appetite = Appetite.valueOf(parts[7].trim().uppercase(Locale.getDefault()))

        return HealthRecord(
            date = date,
            height = height,
            weight = weight,
            heartRate = heartRate,
            bloodPressure = bloodPressure,
            sleepHours = sleepHours,
            wellbeing = wellbeing,
            appetite = appetite
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: StatisticMemberArgs by navArgs()
        val UserDTO = args.UserDTO

        val dates = UserDTO.dates

        for (date in dates) {
            var temp_healthRecord_date = LocalDate.of(date.year, date.month, date.day)
            var temp_height = 0
            var temp_weight = 0.0
            var temp_heartrate = 0
            var temp_pressure = ""
            var temp_wellbeing  = Wellbeing.NOT_STATED
            var temp_appetite = Appetite.NOT_STATED
            var temp_sleep_hours = 0.0

            for (note in date.notes) {
                if (note.type == "HEIGHT") {
                    temp_height = note.value.toInt()
                }
                if (note.type == "WEIGHT") {
                    temp_weight = note.value.toDouble()
                }
                if (note.type == "PULSE") {
                    temp_heartrate = note.value.toInt()
                }
                if (note.type == "PRESSURE") {
                    temp_pressure = note.value
                }
                if (note.type == "SLEEP") {
                    temp_sleep_hours = note.value.toDouble()
                }
                if (note.type == "APPETITE") {
                    temp_appetite = Appetite.valueOf(note.value)
                }
                if (note.type == "WELLBEING") {
                    temp_wellbeing = Wellbeing.valueOf(note.value)
                }
            }
            healthRecords.add(
                HealthRecord(
                    date = temp_healthRecord_date,
                    height = temp_height,
                    weight = temp_weight,
                    heartRate = temp_heartrate,
                    bloodPressure = temp_pressure,
                    sleepHours = temp_sleep_hours,
                    wellbeing = temp_wellbeing,
                    appetite = temp_appetite
                )
            )

        }

        /*
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
        date = LocalDate.of(2024, 5, 20),
        height = 174,
        weight = 69.0,
        heartRate = 780,
        bloodPressure = "140/70",
        sleepHours = 5.0,
        wellbeing = Wellbeing.BAD,
        appetite = Appetite.NO_APPETITE
         */

        Log.d("авава", UserDTO.id.toString() + " " + UserDTO.name)

    }

    private fun calculateAverages(
        text: TextView,
        startDate: LocalDate,
        endDate: LocalDate,
        dataType: DataType
    ) {
        val filteredRecords = healthRecords.filter { it.date in startDate..endDate }

        if (filteredRecords.isEmpty()) {
            text.text = "Записей в указанном периоде нет."
        }

        when (dataType) {
            DataType.HEIGHT -> {
                val averageHeight = filteredRecords.map { it.height }.average()
                text.text = "%.2f см".format(averageHeight)
            }

            DataType.WEIGHT -> {
                val averageWeight = filteredRecords.map { it.weight }.average()
                text.text = "%.2f кг".format(averageWeight)
            }

            DataType.HEART_RATE -> {
                val averageHeartRate = filteredRecords.map { it.heartRate }.average()
                text.text = "%.2f уд/мин".format(averageHeartRate)
            }

            DataType.SLEEP_HOURS -> {
                val averageSleepHours = filteredRecords.map { it.sleepHours }.average()
                text.text = "%.2f часов".format(averageSleepHours)
            }

            DataType.WELLBEING -> {
                val mostCommonWellbeing = filteredRecords.groupBy { it.wellbeing }
                    .maxByOrNull { it.value.size }?.key ?: Wellbeing.NORMAL
                text.text = mostCommonWellbeing.toString()
            }

            DataType.APPETITE -> {
                val mostCommonAppetite = filteredRecords.groupBy { it.appetite }
                    .maxByOrNull { it.value.size }?.key ?: Appetite.NO_APPETITE
                text.text = mostCommonAppetite.toString()
            }

            DataType.PRESSURE -> {
                val regex = "\\d+/\\d+".toRegex()


                val (systolicList, diastolicList) = filteredRecords.filter {
                    it.bloodPressure.matches(regex)
                }.
                map {
                    val (systolic, diastolic) = it.bloodPressure.split("/").map(String::toInt)
                    systolic to diastolic
                }.unzip()

                val averageSystolic = systolicList.average()
                val averageDiastolic = diastolicList.average()
                text.text = "%.2f/%.2f".format(averageSystolic, averageDiastolic)
            }

            else -> {}
        }
    }

    private fun showinfoPieHealth(
        avgtex: TextView, pie: PieChart, dataType: DataType, text: String,
        startDate: LocalDate, endDate: LocalDate
    ) {
        calculateAverages(avgtex, startDate, endDate, dataType)

        val entries = listOf(
            PieEntry(healthRecords.count { it.wellbeing == Wellbeing.GOOD }
                .toFloat(), "Хорошее"),
            PieEntry(healthRecords.count { it.wellbeing == Wellbeing.NORMAL }
                .toFloat(), "Нормальное"),
            PieEntry(healthRecords.count { it.wellbeing == Wellbeing.BAD }
                .toFloat(), "Плохое")
        )
        val dataSet = PieDataSet(entries.filter { it.value !=0f }, text)
        // Задание цветов для сегментов диаграммы
        val customColors = listOf(
            Color.rgb(76, 175, 80),  // Зеленый для "Хорошее"
            Color.rgb(255, 193, 7),  // Желтый для "Нормальное"
            Color.rgb(244, 67, 54)   // Красный для "Плохое"
        )
        dataSet.colors = customColors

        val pieData = PieData(dataSet)
        pie.data = pieData

        // Настройки отображения
        pie.description.isEnabled = false
        pie.isDrawHoleEnabled = false
        pie.setEntryLabelColor(Color.BLACK)
        pie.setEntryLabelTextSize(12f)


        pie.animateY(1000, Easing.EaseInOutQuad)
        pie.invalidate()
    }

    private fun showinfoPieAppetite(
        avgtex: TextView, pie: PieChart, dataType: DataType, text: String,
        startDate: LocalDate, endDate: LocalDate
    ) {
        calculateAverages(avgtex, startDate, endDate, dataType)

        val entries = listOf(
            PieEntry(healthRecords.count { it.appetite == Appetite.NO_APPETITE }
                .toFloat(), "Нет аппетита"),
            PieEntry(
                healthRecords.count { it.appetite == Appetite.GOOD }.toFloat(),
                "Хороший"
            ),
            PieEntry(
                healthRecords.count { it.appetite == Appetite.BAD }.toFloat(),
                "Плохой"
            ),
            PieEntry(healthRecords.count { it.appetite == Appetite.NORMAL }
                .toFloat(), "Нормальный")
        )
        val dataSet = PieDataSet(entries.filter { it.value !=0f }, text)

        val customColors = listOf(
            Color.rgb(76, 175, 80),
            Color.rgb(255, 193, 7),
            Color.rgb(244, 67, 54),
            Color.rgb(199, 21, 133)
        )
        dataSet.colors = customColors

        val pieData = PieData(dataSet)
        pie.data = pieData

        pie.description.isEnabled = false
        pie.isDrawHoleEnabled = false
        pie.setEntryLabelColor(Color.BLACK)
        pie.setEntryLabelTextSize(12f)

        pie.animateY(1000, Easing.EaseInOutQuad)
        pie.invalidate()
    }

    private fun showinfoLine(
        avgtex: TextView, line: LineChart, dataType: DataType, text: String,
        startDate: LocalDate, endDate: LocalDate
    ) {
        calculateAverages(avgtex, startDate, endDate, dataType)

        val entries = healthRecords.mapIndexed { index, record ->
            val value = when (dataType) {
                DataType.WEIGHT -> record.weight.toFloat()
                DataType.HEIGHT -> record.height.toFloat()
                DataType.HEART_RATE -> record.heartRate.toFloat()
                DataType.SLEEP_HOURS -> record.sleepHours.toFloat()
                else -> 0f
            }
            Entry(index.toFloat(), value)
        }
        val dataSet = LineDataSet(entries, text)

        dataSet.color = ColorTemplate.MATERIAL_COLORS[0]
        dataSet.setCircleColor(ColorTemplate.MATERIAL_COLORS[1])
        dataSet.valueTextColor = ColorTemplate.MATERIAL_COLORS[2]
        dataSet.lineWidth = 2.5f
        dataSet.circleRadius = 5f
        dataSet.valueTextSize = 10f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ColorTemplate.MATERIAL_COLORS[3]

        val lineData = LineData(dataSet)
        line.data = lineData

        line.description.isEnabled = false
        line.setTouchEnabled(true)
        line.isDragEnabled = true
        line.setScaleEnabled(true)
        line.setPinchZoom(true)

        line.invalidate()
    }

    private fun showinfoBar(
        avgtex: TextView, bar: BarChart, dataType: DataType, text: String,
        startDate: LocalDate, endDate: LocalDate
    ) {
        calculateAverages(avgtex, startDate, endDate, dataType)

        val entries = healthRecords.mapIndexed { index, record ->
            BarEntry(index.toFloat(), record.height.toFloat())
        }

        val dataSet = BarDataSet(entries, text)
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val barData = BarData(dataSet)

        bar.data = barData
        bar.description.isEnabled = false
        bar.animateY(1000)
        bar.invalidate()
    }

    private fun showinfoPressure(
        avgPressureText: TextView, pressure: LineChart, dataType: DataType,
        startDate: LocalDate, endDate: LocalDate
    ) {
        calculateAverages(avgPressureText, startDate, endDate, dataType)
        val systolicEntries = mutableListOf<Entry>()
        val diastolicEntries = mutableListOf<Entry>()

        healthRecords.forEachIndexed { index, record ->
            val pressure_temp = record.bloodPressure
            val regex = "\\d+/\\d+".toRegex()
            if (pressure_temp.matches(regex)) {
                val (systolic, diastolic) = record.bloodPressure.split("/").map { it.toFloat() }
                systolicEntries.add(Entry(index.toFloat(), systolic))
                diastolicEntries.add(Entry(index.toFloat(), diastolic))
            }
        }

        val systolicDataSet = LineDataSet(systolicEntries, "Верхнее артериальное")
        systolicDataSet.color = Color.RED
        systolicDataSet.valueTextColor = Color.BLACK

        val diastolicDataSet = LineDataSet(diastolicEntries, "Нижнее сердечное")
        diastolicDataSet.color = Color.BLUE
        diastolicDataSet.valueTextColor = Color.BLACK

        val lineData = LineData(systolicDataSet, diastolicDataSet)

        pressure.data = lineData
        pressure.description.isEnabled = false
        pressure.animateY(1000)
        pressure.invalidate()
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
            6 -> uptFellings
            else -> false
        }
    }

    private fun getDateFromEditText(editText: EditText): LocalDate {
        val dateString = editText.text.toString()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") // Формат даты
        return LocalDate.parse(dateString, formatter)
    }



    private fun update(
        page: Int, next: Boolean, avgPressure: LineChart, avgAppetitText: TextView,
        avgFeelingsText: TextView, avgHeight: BarChart, avgWeight: LineChart,
        avgCHSS: LineChart, avgSleep: LineChart, avgHeightText: TextView,
        avgWeightText: TextView, avgCHSSText: TextView, avgSleepText: TextView,
        avgPressureText: TextView, avgFellings: PieChart, avgAppetite: PieChart,
        startDate: LocalDate, endDate: LocalDate
    ) {

        var realnumber = page
        if (next) {
            if (page == 7) {
                realnumber = 0
            } else {
                realnumber++
            }
        }
        Log.d("dada", "$realnumber $next")
        if (realnumber == 3) {
            showinfoPressure(
                avgPressureText,
                avgPressure,
                DataType.PRESSURE,
                startDate,
                endDate
            )
        } else if (realnumber == 4) {
            showinfoPieAppetite(
                avgAppetitText,
                avgAppetite,
                DataType.APPETITE,
                "Аппетит",
                startDate,
                endDate
            )
        } else if (realnumber == 6) {
            showinfoPieHealth(
                avgFeelingsText,
                avgFellings,
                DataType.WELLBEING,
                "Самочувствие",
                startDate,
                endDate
            )
        } else if (realnumber == 0) {
            showinfoBar(
                avgHeightText,
                avgHeight,
                DataType.HEIGHT,
                "Рост",
                startDate,
                endDate
            )
        } else if (realnumber == 1) {
            showinfoLine(
                avgWeightText,
                avgWeight,
                DataType.WEIGHT,
                "Вес",
                startDate,
                endDate
            )
        } else if (realnumber == 2) {
            showinfoLine(
                avgCHSSText,
                avgCHSS,
                DataType.HEART_RATE,
                "ЧСС",
                startDate,
                endDate
            )
        } else if (realnumber == 5) {
            showinfoLine(
                avgSleepText,
                avgSleep,
                DataType.SLEEP_HOURS,
                "Сон",
                startDate,
                endDate
            )
        }
    }
}