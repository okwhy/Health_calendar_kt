package com.sus.calendar.services
import com.sus.calendar.dtos.DateWithIdNotesUidDto
import com.sus.calendar.dtos.GroupforUserDto


import com.sus.calendar.dtos.UserDTO
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import com.sus.calendar.dtos.tmpSolution.DataWithNotesTmp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/users/login")
    fun login(@Query("login") login: String,@Query("password") password: String): Call<UserDTO>

    @GET("/api/groups/byuser/{id}")
    fun get_member_groups(@Path("id") id: Long): Call<List<GroupforUserDto>>

    @GET("/api/groups/users/{user_id}")
    fun get_creator_groups(@Path("user_id") user_id:Long): Call<List<GroupCreatorForCreatorDto>>

    @POST("api/users/register")
    fun register(@Query("login") login: String,@Query("password") password: String,@Query("name") name: String): Call<Boolean>

    @POST("/api/groups/add")
    fun create_group(@Query("creator_id") creator_id: Long,@Query("name") name: String): Call<GroupCreatorForCreatorDto>

    @POST("/api/groups/users/add")
    fun addUser(@Query("id") id:Long,@Query("key") key:String):Call<GroupforUserDto>

    @DELETE("api/groups/users/{user_id}")
    fun delete_user(@Path("user_id") userid:Long,@Query("id") id:Long):Call<Void>
    @DELETE("api/groups/delete/{id}")
    fun delete_group(@Path("id") id:Long):Call<Void>
    @GET("api/dates/user/{id}")
    fun getUserDats(@Path("id") id:Long):Call<List<DateWithIdNotesUidDto>>
    @POST("/api/dates/add/{id}")
    fun addDate(@Path("id") id:Long,@Body datedto:DataWithNotesTmp):Call<DateWithIdNotesUidDto>
    @POST("/api/dates/update/{id}")
    fun updateDate(@Path("id") id:Long,@Body datedto:DataWithNotesTmp):Call<DateWithIdNotesUidDto>
}
