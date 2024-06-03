package com.sus.calendar.dtos.getgroupcreator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMemberForCreatorDto(
    val fkUser: UserInGroupDto
):Parcelable
