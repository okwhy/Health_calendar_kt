package com.sus.calendar.dtos

import com.sus.calendar.dtos.getgroupcreator.NoteWithIdDto


data class DateWithIdNotesUidDto(
    val id: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    var notes: Set<NoteWithIdDto>,
    var uid: String
)