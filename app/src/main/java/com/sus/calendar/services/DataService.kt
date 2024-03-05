package com.sus.calendar.services

import android.content.Context
import com.sus.calendar.daos.DateDao
import com.sus.calendar.daos.NoteDao
import com.sus.calendar.database.AppDatabase
import com.sus.calendar.dtos.PressureValue
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.DateWithNotes
import com.sus.calendar.entities.Note

class DataService private constructor(context: Context) {
    private val dateDao: DateDao
    private val noteDao: NoteDao
    val noteCategories: Set<String> = HashSet(
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

    init {
        val appDatabase = AppDatabase.getInstance(context)
        dateDao = appDatabase?.dateDao()!!
        noteDao = appDatabase.noteDao()!!
    }

    fun GetAllData(): List<DateSQL?>? {
        return dateDao.all
    }

    fun GetByYear(year: Int): List<DateSQL?>? {
        return dateDao.getByYear(year)
    }

    fun GetByMonth(year: Int, month: Int): List<DateSQL?>? {
        return dateDao.getByMonth(year, month)
    }

    fun getDaysByMonth(year: Int, month: Int): List<Int> {
        return dateDao.getDaysByMonth(year, month) as List<Int>
    }

    fun GetAll(): List<Note?>? {
        return noteDao.all
    }

    fun GetByCat(type: String): List<Note>? {
        return if (noteCategories.contains(type)) noteDao.getByCat(type) as List<Note>? else null
    }

    fun GetByCat(type: String, value: Double): List<Note>? {
        return if (noteCategories.contains(type)) noteDao.getByCat(type, value) as List<Note>? else null
    }

    fun getDate(year: Int, mouth: Int, day: Int): DateWithNotes? {
        return dateDao.getDate(year, mouth, day)
    }

    fun getDateNoNotes(year: Int, mouth: Int, day: Int): DateSQL? {
        return dateDao.getDateNoNotes(year, mouth, day)
    }

    fun insertDate(year: Int, mouth: Int, day: Int): Long {
        return dateDao.insert(DateSQL(year, mouth, day))
    }

    fun getBetween(
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): List<DateWithNotes?>? {
        return dateDao.getBetween(byear, ayear, bmonth, amonth, bdate, adate)
    }

    fun insertOrUpdateNote(note: Note): Long {
        val note1 = noteDao.getByCatAndFKId(note.type, note.id_fkdate)
        return if (note1 != null) {
            note1.value = note.value
            noteDao.update(note1)
            note1.id
        } else {
            noteDao.insert(note)
            note.id
        }
    }

    val allNoteTest: List<Note?>?
        get() = noteDao.all
    val allDateTest: List<DateSQL?>?
        get() = dateDao.all

    fun getDateById(id: Long): DateSQL? {
        return dateDao.getDateById(id)
    }

    fun getMediumByDateandtype(
        type: String?, byear: Int, ayear: Int,
        bmonth: Int, amonth: Int, bdate: Int, adate: Int
    ): Float {
        return dateDao.getAVGNotesByTypeBetweenDates(
            type,
            byear,
            ayear,
            bmonth,
            amonth,
            bdate,
            adate
        )
    }

    fun getNotesByDateAndTypeS(
        type: String?, byear: Int, ayear: Int,
        bmonth: Int, amonth: Int, bdate: Int, adate: Int
    ): List<String?>? {
        return dateDao.getNotesByTypeBetweenDatesNoCast(
            type,
            byear,
            ayear,
            bmonth,
            amonth,
            bdate,
            adate
        )
    }

    fun getNotesByDateAndTypeF(
        type: String?, byear: Int, ayear: Int,
        bmonth: Int, amonth: Int, bdate: Int, adate: Int
    ): List<Float?>? {
        return dateDao.getNotesByTypeBetweenDatesCast(
            type,
            byear,
            ayear,
            bmonth,
            amonth,
            bdate,
            adate
        )
    }

    fun getPressureByDate(
        byear: Int, ayear: Int,
        bmonth: Int, amonth: Int, bdate: Int, adate: Int
    ): List<PressureValue> {
        val pressureStrings = dateDao.getNotesByTypeBetweenDatesNoCast(
            "PRESSURE",
            byear,
            ayear,
            bmonth,
            amonth,
            bdate,
            adate
        )
        val pressureValues: MutableList<PressureValue> = ArrayList()
        if (pressureStrings != null) {
            for (s in pressureStrings) {
                val vals = s?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
                pressureValues.add(PressureValue(vals!![0].toInt(), vals[1].toInt()))
            }
        }
        return pressureValues
    }

    fun getMostCommonNote(
        type: String?, byear: Int, ayear: Int,
        bmonth: Int, amonth: Int, bdate: Int, adate: Int
    ): String? {
        return dateDao.getMostCommonNote(type, byear, ayear, bmonth, amonth, bdate, adate)
    }

    companion object {
        private var dataService: DataService? = null
        fun initial(context: Context): DataService? {
            if (dataService == null) dataService = DataService(context)
            return dataService
        }
    }
}
