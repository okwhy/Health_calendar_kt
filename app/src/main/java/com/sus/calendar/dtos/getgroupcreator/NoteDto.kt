package com.sus.calendar.dtos.getgroupcreator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteDto(
    val type: String,
    val value: String
):Parcelable
