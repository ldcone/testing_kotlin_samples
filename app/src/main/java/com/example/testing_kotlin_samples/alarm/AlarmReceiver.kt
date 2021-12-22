package com.example.testing_kotlin_samples.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.testing_kotlin_samples.R

class AlarmReceiver:BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }
    override fun onReceive(p0: Context, p1: Intent) {

        createNotificationChannel(p0)
        notifyNotification(p0)
        Log.d("alarm","alarming koko")



    }

    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)){
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText("일어날 시간입니다.")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH
                )
            notify(NOTIFICATION_ID,build.build())

        }
    }

    private fun createNotificationChannel(p0: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "기상 알람",
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(p0).createNotificationChannel(notificationChannel)
        }
    }
}