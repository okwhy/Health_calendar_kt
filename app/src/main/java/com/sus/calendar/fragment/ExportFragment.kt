package com.sus.calendar.fragment

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.sus.calendar.MainActivity
import com.sus.calendar.R
import com.sus.calendar.RetrofitClient
import com.sus.calendar.dtos.DateWithIdNotesUidDto
import com.sus.calendar.dtos.getgroupcreator.NoteWithIdDto
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.DateWithNotes
import com.sus.calendar.entities.Note
import com.sus.calendar.services.DataService
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class ExportFragment : Fragment() {
    private var byear = 0
    private var ayear = 0
    private var bmonth = 0
    private var amonth = 0
    private var bday = 0
    private var aday = 0
    var dateOt: EditText? = null
    var dateDo: EditText? = null
    var datePickerDialogOt: DatePickerDialog? = null
    var datePickerDialogDo: DatePickerDialog? = null
    private var data:MutableList<DateWithIdNotesUidDto> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.export_page, container, false)
        val buttonCreateExcel = view.findViewById<Button>(R.id.buttonCreateExcel)
        loaddata()
        dateOt = view.findViewById<View>(R.id.dateOt) as EditText
        dateDo = view.findViewById<View>(R.id.dateDo) as EditText
        dateOt!!.setText("")
        dateDo!!.setText("")
        dateOt!!.setOnClickListener {
            val c = Calendar.getInstance()
            val mDay = c[Calendar.DAY_OF_MONTH]
            val mMonth = c[Calendar.MONTH]
            val mYear = c[Calendar.YEAR]
            datePickerDialogOt =
                DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    dateOt!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    byear = year
                    bmonth = monthOfYear + 1
                    bday = dayOfMonth
                }, mYear, mMonth, mDay)
            datePickerDialogOt!!.show()
        }
        dateDo!!.setOnClickListener {
            val c = Calendar.getInstance()
            val mDay = c[Calendar.DAY_OF_MONTH]
            val mMonth = c[Calendar.MONTH]
            val mYear = c[Calendar.YEAR]
            datePickerDialogDo =
                DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    dateDo!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    ayear = year
                    amonth = monthOfYear + 1
                    aday = dayOfMonth
                }, mYear, mMonth, mDay)
            datePickerDialogDo!!.show()
        }
        buttonCreateExcel.setOnClickListener { createExcelFile() }
        return view
    }

    private fun createExcelFile() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                "android.permission.READ_EXTERNAL_STORAGE"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf("android.permission.READ_EXTERNAL_STORAGE"),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val baseFileName = "Health_Diary"
        val file = getUniqueFile(downloadsDir, baseFileName, "xls")
        Log.d("Kd", "$byear $ayear $bmonth $amonth $bday $aday")
        val dataService = this.context?.let { DataService.initial(it) }
        val startdate=LocalDate.of(byear,bmonth,bday).minusDays(1)
        val enddate=LocalDate.of(ayear,amonth,aday).plusDays(1)
        val filtereddates=data.filter {
            val currentdate=LocalDate.of(it.year,it.month,it.day)
            currentdate.isAfter(startdate)&&currentdate.isBefore(enddate)
        }.sortedByDescending { LocalDate.of(it.year, it.month, it.day) }
        if (filtereddates.isEmpty()) {
            Toast.makeText(activity, "Записи за данный день отсутствуют", Toast.LENGTH_SHORT)
                .show()
            return
        }


        val workbook = Workbook.createWorkbook(FileOutputStream(file))
        val sheet = workbook.createSheet("Дневник здоровья", 0)


        var label = Label(0, 0, "Дата")
        sheet.addCell(label)
        label = Label(1, 0, "Рост (см)")
        sheet.addCell(label)
        label = Label(2, 0, "Вес (кг)")
        sheet.addCell(label)
        label = Label(3, 0, "ЧСС (уд/мин) в покое")
        sheet.addCell(label)
        label = Label(4, 0, "Давление (А/Д)")
        sheet.addCell(label)
        label = Label(5, 0, "Аппетит")
        sheet.addCell(label)
        label = Label(6, 0, "Сон")
        sheet.addCell(label)
        label = Label(7, 0, "Самочувствие")
        sheet.addCell(label)
        var rown = 1
        var refDate = LocalDate.of(ayear, amonth, aday)
        val endDate = LocalDate.of(byear, bmonth, bday).minusDays(1)
        for (dwn in filtereddates) {
            while (!refDate.isEqual(LocalDate.of(dwn.year,dwn.month,dwn.day))) {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy")
                label = Label(0, rown, refDate.format(dateTimeFormatter))
                sheet.addCell(label)
                for (i in 1 until sheet.columns) {
                    label = Label(i, rown, "-")
                    sheet.addCell(label)
                }
                refDate = refDate.minusDays(1)
                rown++
            }
            label = Label(0, rown, "${dwn.day}-${dwn.month}-${dwn.year}")
            sheet.addCell(label)
            val notes: MutableList<NoteWithIdDto> = ArrayList()
            dwn.notes.let { notes.addAll(it.toMutableList()) }
            val cellval: MutableMap<String?, String?> = hashMapOf()
            val catgs: MutableSet<String> = HashSet()
            catgs.addAll(dataService!!.noteCategories)
            for (s in catgs) {
                var value = "-"
                val refNote = notes.stream().filter { x -> x!!.type == s }
                    .findFirst().orElse(null)
                if (refNote != null && refNote.value !== "Нет данных") value =
                    refNote.value
                cellval[s] = value
            }
            label = Label(1, rown, cellval["HEIGHT"])
            sheet.addCell(label)
            label = Label(2, rown, cellval["WEIGHT"])
            sheet.addCell(label)
            label = Label(3, rown, cellval["PULSE"])
            sheet.addCell(label)
            label = Label(4, rown, cellval["PRESSURE"])
            sheet.addCell(label)
            label = Label(5, rown, cellval["APPETITE"])
            sheet.addCell(label)
            label = Label(6, rown, cellval["SLEEP"])
            sheet.addCell(label)
            label = Label(7, rown, cellval["HEALTH"])
            sheet.addCell(label)
            rown++
            refDate = refDate.minusDays(1)
        }
        while (!refDate.isEqual(endDate)) {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy")
            label = Label(0, rown, refDate.format(dateTimeFormatter))
            sheet.addCell(label)
            for (i in 1 until sheet.columns) {
                label = Label(i, rown, "-")
                sheet.addCell(label)
            }
            rown++
            refDate = refDate.minusDays(1)
        }
        for (i in 0 until sheet.columns) {
            setColumnWidth(sheet, i)
        }
        workbook.write()
        workbook.close()
        Toast.makeText(activity, "Файл Excel создан успешно", Toast.LENGTH_SHORT).show()
    }

    private fun getUniqueFile(directory: File, baseName: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var fileName = baseName + "_" + timeStamp + "." + extension
        var file = File(directory, fileName)
        var count = 1
        while (file.exists()) {
            fileName = baseName + "_" + timeStamp + "_" + count + "." + extension
            file = File(directory, fileName)
            count++
        }
        return file
    }

    private fun setColumnWidth(sheet: WritableSheet, columnIndex: Int) {
        var maxContentLength = 0

        for (i in 0 until sheet.rows) {
            val content = sheet.getCell(columnIndex, i).contents
            maxContentLength = Math.max(maxContentLength, content.length)
        }
        sheet.setColumnView(
            columnIndex,
            maxContentLength + 10
        )
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }
    private fun loaddata(){
        val apiService=RetrofitClient.instance
        val callGetDates=apiService.getUserDats(MainActivity.DataManager.getUserData()!!.id)
        callGetDates.enqueue(object : Callback<List<DateWithIdNotesUidDto>> {
            override fun onResponse(call: Call<List<DateWithIdNotesUidDto>>, response: Response<List<DateWithIdNotesUidDto>>) {
                if (response.isSuccessful) {
                    data.addAll(response.body()!!)
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
