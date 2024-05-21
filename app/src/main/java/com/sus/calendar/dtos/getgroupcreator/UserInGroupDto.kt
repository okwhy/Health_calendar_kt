package com.sus.calendar.dtos.getgroupcreator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInGroupDto(
    val id: Long,
    val name: String,
    val dates: List<DateDto>
):Parcelable
