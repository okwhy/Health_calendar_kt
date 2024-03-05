package com.sus.calendar

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
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.DateWithNotes
import com.sus.calendar.entities.Note
import com.sus.calendar.services.DataService
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.export_page, container, false)
        val buttonCreateExcel = view.findViewById<Button>(R.id.buttonCreateExcel)
        dateOt = view.findViewById<View>(R.id.dateOt) as EditText
        dateDo = view.findViewById<View>(R.id.dateDo) as EditText
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
        // Создание нового файла Excel
        val androidVersion = Build.VERSION.RELEASE
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                "android.permission.READ_EXTERNAL_STORAGE"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешения нет, запросим его у пользователя
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
        val ref = arrayOfNulls<DateSQL>(1)
        val id = LongArray(1)
        try {
            val dateWithNotes: Array<List<DateWithNotes>?> = arrayOfNulls(1)
            val runnable = Runnable {
                dateWithNotes[0] = dataService?.getBetween(byear, ayear, bmonth, amonth, bday, aday) as List<DateWithNotes>?
            }
            val thread = Thread(runnable)
            thread.start()
            thread.join()
            if (dateWithNotes[0] == null || dateWithNotes[0]!!.size == 0) {
                Toast.makeText(activity, "Записи за данный день отсутствуют", Toast.LENGTH_SHORT)
                    .show()
                return
            }


            // Создание рабочей книги и листа
            val workbook = Workbook.createWorkbook(FileOutputStream(file))
            val sheet = workbook.createSheet("Дневник здоровья", 0)

            // Запись данных в ячейки
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
            val endDate = LocalDate.of(byear, bmonth, bday)
            for (dwn in dateWithNotes[0]!!) {
                while (!refDate.isEqual(dwn.dateSQL?.asLocalDate)) {
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
                //                Log.d("fd",tday+" "+tmonth+" "+tyear);
                label = Label(0, rown, dwn.dateSQL?.dateString )
                sheet.addCell(label)
                val notes: MutableList<Note?> = ArrayList()
                dwn.notes?.let { notes.addAll(it) }
                val cellval: MutableMap<String?, String?> = hashMapOf()
                val catgs: MutableSet<String> = HashSet()
                catgs.addAll(dataService!!.noteCategories)
                for (s in catgs) {
                    var value = "-"
                    val refNote = notes.stream().filter { x: Note? -> x!!.type == s }
                        .findFirst().orElse(null)
                    if (refNote != null && refNote.value !== "Нет данных") value = refNote.value.toString()
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
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(activity, "Ошибка при создании файла Excel", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUniqueFile(directory: File, baseName: String, extension: String): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var fileName = baseName + "_" + timeStamp + "." + extension
        var file = File(directory, fileName)

        // Проверка на уникальность имени файла, добавление числа, если файл уже существует
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

        // Находим максимальную длину данных в столбце
        for (i in 0 until sheet.rows) {
            val content = sheet.getCell(columnIndex, i).contents
            maxContentLength = Math.max(maxContentLength, content.length)
        }

        // Устанавливаем ширину столбца на основе максимальной длины данных
        sheet.setColumnView(
            columnIndex,
            maxContentLength + 2
        ) // Увеличиваем ширину на 2 символа для дополнительного пространства
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }
}
