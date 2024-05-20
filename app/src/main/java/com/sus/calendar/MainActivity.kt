package com.sus.calendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sus.calendar.databinding.ActivityMainBinding
import com.sus.calendar.dtos.UserDTO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val BASE_URL = "http://192.168.0.102:1337/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}


class MainActivity : AppCompatActivity() {





//    var bottomNavigationView: BottomNavigationView? = null
//    val Calendar_page = R.id.homepage
//    val Export_page = R.id.export
    private lateinit var binding: ActivityMainBinding
//    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("key_id")){
            DataManager.setUserData(
                UserDTO(sharedPreferences.getLong("key_id",-3)
                , sharedPreferences.getString("key_name","")!!
                ))
        }

//        setRepeatingAlarm()
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        appBarConfiguration =AppBarConfiguration(
//            setOf(
//                R.id.nav_calendar,R.id.nav_export
//            )
//        )
//        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    private fun setRepeatingAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Создаем Intent для нашего BroadcastReceiver
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Устанавливаем интервал в 24 часа
        val repeatInterval = (60 * 60 * 1000).toLong() // час в миллисекундах

        // Устанавливаем повторяющееся событие
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + repeatInterval,
            repeatInterval,
            pendingIntent
        )
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
    //    @SuppressLint("NonConstantResourceId")
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        return if (item.itemId == Calendar_page) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.flFragment, calendarFragment)
//                .commit()
//            true
//        } else if (item.itemId == Export_page) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.flFragment, exportFragment)
//                .commit()
//            true
//        } else false
//    }
}