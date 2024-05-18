package com.sus.calendar

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var bottomNavigationView: BottomNavigationView? = null
    val Calendar_page = R.id.homepage
    val Export_page = R.id.export
    val Statistic_page = R.id.statisticpage
    var Account_page = R.id.account

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRepeatingAlarm()
        bottomNavigationView = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView!!
            .setOnNavigationItemSelectedListener(this)
        bottomNavigationView!!.selectedItemId = R.id.homepage
    }

    var calendarFragment = CalendarFragment()
    var exportFragment = ExportFragment()
    var statisticFragment = StatisticFragment()

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

    @SuppressLint("NonConstantResourceId")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == Calendar_page) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flFragment, calendarFragment)
                .commit()
            true
        } else if (item.itemId == Export_page) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.flFragment, exportFragment)
                .commit()
            true
        } else if (item.itemId == Statistic_page) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flFragment, statisticFragment)
                    .commit()
            true
        } else if (item.itemId == Account_page) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flFragment, accountFragment)
                    .commit()
            true
        }else false
    }
}