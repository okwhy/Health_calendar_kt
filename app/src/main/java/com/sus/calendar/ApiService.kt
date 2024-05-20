package com.sus.calendar
import com.sus.calendar.dtos.GroupforUserDto


import com.sus.calendar.dtos.UserDTO
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import retrofit2.Call
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

    @DELETE("api/groups/users/{user_id}")
    fun delete_user(@Path("user_id") userid:Long,@Query("id") id:Long):Call<Void>
}
