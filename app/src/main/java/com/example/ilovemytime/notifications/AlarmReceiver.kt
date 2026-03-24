package com.example.ilovemytime.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.ilovemytime.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getStringExtra("EXTRA_TASK_ID") ?: return
        val taskName = intent.getStringExtra("EXTRA_TASK_NAME") ?: "Tu actividad"
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "¡Es hora de tu actividad!"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "ilovemytime_channel_v3"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de Tareas",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("DESTINATION", "alarm_ringing")
            putExtra("TASK_ID", taskId)
            putExtra("TASK_NAME", taskName)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            taskId.hashCode(),
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(taskName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(taskId.hashCode(), notification)
    }
}