package com.sus.calendar.entities

import androidx.room.Embedded
import androidx.room.Relation



class DateWithNotes {
    @JvmField
    @Embedded
    var dateSQL: DateSQL? = null

    @JvmField
    @Relation(parentColumn = "id", entityColumn = "id_fkdate")
    var notes: List<Note>? = null
}
