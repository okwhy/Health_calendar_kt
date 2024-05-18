package com.sus.calendar

import ApiService
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sus.calendar.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

   // private const val BASE_URL = "http://192.168.115.110:1337/"

    private const val BASE_URL = "http://10.139.51.253:1337/"

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
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    var calendarFragment = CalendarFragment()
    var exportFragment = ExportFragment()
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