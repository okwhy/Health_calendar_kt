package com.sus.calendar


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sus.calendar.databinding.ActivityMainBinding
import com.sus.calendar.dtos.UserDTO
import com.sus.calendar.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.104:1337/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("key_id")){
            DataManager.setUserData(
                UserDTO(sharedPreferences.getLong("key_id",-3)
                    , sharedPreferences.getString("key_name","")!!
                ))
        }
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }
   object DataManager {
        private var user: UserDTO?=null
        fun setUserData(user: UserDTO?){
            this.user=user
        }
        fun getUserData():UserDTO?{
            return user
        }
   }

}