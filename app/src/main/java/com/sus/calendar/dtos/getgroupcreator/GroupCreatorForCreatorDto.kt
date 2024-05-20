package com.sus.calendar.dtos.getgroupcreator

data class GroupCreatorForCreatorDto(
    val id:Long,
    val groupName: String,
    val accessKey: String,
    val groupMembers: Set<GroupMemberForCreatorDto>
)
