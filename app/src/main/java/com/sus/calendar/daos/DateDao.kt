package com.sus.calendar.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sus.calendar.entities.DateSQL
import com.sus.calendar.entities.DateWithNotes

@Dao
interface DateDao {
    @get:Query("select * from DateSQL")
    val all: List<DateSQL?>?

    @Delete
    fun delete(vararg dates: DateSQL)

    @Insert
    fun insert(dateSQL: DateSQL): Long

    @Query("select * from DateSQL where DateSQL.year =:year order by month,day")
    fun getByYear(year: Int): List<DateSQL?>?

    @Query("select * from DateSQL where DateSQL.year =:year and DateSQL.month =:month order by day")
    fun getByMonth(year: Int, month: Int): List<DateSQL?>?

    @Query("select day from DateSQL where DateSQL.year =:year and DateSQL.month =:month order by day")
    fun getDaysByMonth(year: Int, month: Int): List<Int?>?

    @Query(
        "SELECT * FROM DateSQL WHERE DateSQL.year BETWEEN :byear AND :ayear AND " +
                "DateSQL.month BETWEEN :bmonth and :amonth AND DateSQL.day BETWEEN" +
                ":bdate and :adate"
    )
    fun getBetween(
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): List<DateWithNotes?>?

    @Query("select * from DateSQL where DateSQL.year =:year and DateSQL.month =:month and DateSQL.day =:day")
    fun getDate(year: Int, month: Int, day: Int): DateWithNotes?

    @Query("select * from DateSQL where DateSQL.year =:year and DateSQL.month =:month and DateSQL.day =:day")
    fun getDateNoNotes(year: Int, month: Int, day: Int): DateSQL?

    @Query("select * from DateSQL where DateSQL.id=:id")
    fun getDateById(id: Long): DateSQL?

    @Query(
        "select AVG(CAST(Note.value as float)) from DateSQL join Note on Note.id_fkdate=DateSQL.id " +
                "where Note.type=:type and DateSQL.year BETWEEN :byear AND :ayear AND " +
                "DateSQL.month BETWEEN :bmonth and :amonth AND DateSQL.day BETWEEN" +
                ":bdate and :adate"
    )
    fun getAVGNotesByTypeBetweenDates(
        type: String?,
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): Float

    @Query(
        "select Note.value from DateSQL join Note on Note.id_fkdate=DateSQL.id " +
                "where Note.type=:type and DateSQL.year BETWEEN :byear AND :ayear AND " +
                "DateSQL.month BETWEEN :bmonth and :amonth AND DateSQL.day BETWEEN" +
                ":bdate and :adate"
    )
    fun getNotesByTypeBetweenDatesNoCast(
        type: String?,
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): List<String?>?

    @Query(
        "select CAST(Note.value as float) from DateSQL join Note on Note.id_fkdate=DateSQL.id " +
                "where Note.type=:type and DateSQL.year BETWEEN :byear AND :ayear AND " +
                "DateSQL.month BETWEEN :bmonth and :amonth AND DateSQL.day BETWEEN" +
                ":bdate and :adate"
    )
    fun getNotesByTypeBetweenDatesCast(
        type: String?,
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): List<Float?>?

    @Query(
        "select value  from DateSQL join Note on Note.id_fkdate=DateSQL.id " +
                "where Note.type=:type and DateSQL.year BETWEEN :byear AND :ayear AND " +
                "DateSQL.month BETWEEN :bmonth and :amonth AND DateSQL.day BETWEEN" +
                ":bdate and :adate GROUP BY value order by value LIMIT 1"
    )
    fun getMostCommonNote(
        type: String?,
        byear: Int,
        ayear: Int,
        bmonth: Int,
        amonth: Int,
        bdate: Int,
        adate: Int
    ): String?
}