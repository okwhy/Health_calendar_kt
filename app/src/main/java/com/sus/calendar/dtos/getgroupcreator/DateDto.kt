package com.sus.calendar.dtos.getgroupcreator

data class DateDto(
    val year: Int ,
    val month: Int,
    val day: Int,
    val notes: List<NoteDto>
)
