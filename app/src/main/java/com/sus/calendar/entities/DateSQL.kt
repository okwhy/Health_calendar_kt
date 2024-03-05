package com.sus.calendar.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity
class DateSQL : Serializable {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @JvmField
    var year: Int
    @JvmField
    var month: Int
    @JvmField
    var day: Int

    @Ignore
    constructor(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    constructor(id: Long, year: Int, month: Int, day: Int) {
        this.id = id
        this.year = year
        this.month = month
        this.day = day
    }

    @get:Ignore
    val dateString: String
        get() = "$day-$month-$year"

    @get:Ignore
    val asLocalDate: LocalDate
        get() = LocalDate.of(year, month, day)
}