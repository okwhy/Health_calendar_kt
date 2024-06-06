package com.sus.calendar.dtos.tmpSolution


data class DataWithNotesTmp(
    val year: Int,
    val month: Int,
    val day: Int,
    var notes: Set<NoteTmp>,
    var uid: String
)