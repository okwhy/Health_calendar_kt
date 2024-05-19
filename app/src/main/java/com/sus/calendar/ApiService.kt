package com.sus.calendar

import com.sus.calendar.dtos.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/users/login")
    fun login(@Query("login") login: String,@Query("password") password: String): Call<UserDTO>

    @POST("api/users/register")
    fun register(@Query("login") login: String,@Query("password") password: String,@Query("name") name: String): Call<Boolean>
}