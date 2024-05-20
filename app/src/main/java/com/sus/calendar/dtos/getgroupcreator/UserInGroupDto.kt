package com.sus.calendar.dtos.getgroupcreator

data class UserInGroupDto(
    val id: Long,
    val name: String,
    val dates: List<DateDto>
)
