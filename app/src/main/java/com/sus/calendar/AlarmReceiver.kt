package com.sus.calendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Запуск службы при получении события от AlarmManager
        val serviceIntent = Intent(context, UserNotificationService::class.java)
        context.startForegroundService(serviceIntent)
    }
}