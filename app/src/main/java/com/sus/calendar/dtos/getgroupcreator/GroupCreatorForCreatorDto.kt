package com.sus.calendar.dtos.getgroupcreator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupCreatorForCreatorDto(
    val id:Long,
    val groupName: String,
    val accessKey: String,
    val groupMembers: Set<GroupMemberForCreatorDto>
): Parcelable
