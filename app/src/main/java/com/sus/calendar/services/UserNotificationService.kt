package com.sus.calendar.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sus.calendar.R

//class UserNotificationService : Service() {
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    var powerManager = getSystemService(POWER_SERVICE) as PowerManager
//    var wakeLock = powerManager.newWakeLock(
//        PowerManager.PARTIAL_WAKE_LOCK,
//        "YourApp::WakeLock"
//    )
//
//    override fun onCreate() {
//        super.onCreate()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel()
//        }
//    }
//
//    // Создание канала уведомлений (для Android 8.0 и выше)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name: CharSequence = "Channel Name"
//            val description = "Channel Description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel("CHANNEL_ID", name, importance)
//            channel.description = description
//            val notificationManager = getSystemService(
//                NotificationManager::class.java
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        // Здесь разместите код для отправки уведомления
//        sendNotification()
//
//        // Возвращаем значение, указывающее, как следует взаимодействовать с службой в случае ее остановки
//        return START_NOT_STICKY
//    }
//
//    // Метод для отправки уведомления
//    private fun sendNotification() {
//        // Создаем уведомление
//        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
//            .setSmallIcon(R.drawable.statisticpage_icon)
//            .setContentTitle("Заголовок уведомления")
//            .setContentText("Текст уведомления")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        // Отправляем уведомление
//        val notificationManager = NotificationManagerCompat.from(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        val notificationId = 42
//        notificationManager.notify(notificationId, builder.build())
//    }
//}