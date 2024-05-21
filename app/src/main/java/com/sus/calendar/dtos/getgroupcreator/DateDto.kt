package com.sus.calendar.dtos.getgroupcreator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateDto(
    val year: Int ,
    val month: Int,
    val day: Int,
    val notes: List<NoteDto>
):Parcelable
