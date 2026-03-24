package com.example.ilovemytime.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(taskId: String, taskName: String, timeString: String?) {
        if (timeString.isNullOrBlank()) {
            Toast.makeText(context, "Error: Hora vacía", Toast.LENGTH_SHORT).show()
            return
        }

        val timeParts = parseTimeString(timeString)
        if (timeParts == null) {
            Toast.makeText(context, "Error: Formato de hora no válido. Usa ej. 8:00 AM", Toast.LENGTH_LONG).show()
            return
        }

        val (hour, minute) = timeParts

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_TASK_ID", taskId)
            putExtra("EXTRA_TASK_NAME", taskName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            val amPm = if (hour >= 12) "PM" else "AM"
            val hr12 = if (hour % 12 == 0) 12 else hour % 12
            val minStr = minute.toString().padStart(2, '0')
            Toast.makeText(context, "Alarma guardada a las $hr12:$minStr $amPm", Toast.LENGTH_LONG).show()

        } catch (e: SecurityException) {
            Toast.makeText(context, "Permiso de alarma denegado por el sistema", Toast.LENGTH_LONG).show()
        }
    }

    private fun parseTimeString(time: String): Pair<Int, Int>? {
        return try {
            val cleanTime = time.trim().lowercase()
            val isPM = cleanTime.contains("pm")
            val numbersPart = cleanTime.replace(Regex("[^0-9:]"), "")
            val parts = numbersPart.split(":")

            if (parts.size < 2) return null

            var hour = parts[0].toInt()
            val minute = parts[1].toInt()

            if (isPM && hour < 12) hour += 12
            if (!isPM && hour == 12) hour = 0

            Pair(hour, minute)
        } catch (e: Exception) {
            null
        }
    }

    fun scheduleTimerAlarm(taskId: String, title: String, taskName: String, delaySeconds: Int) {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, delaySeconds)
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_TASK_ID", taskId)
            putExtra("EXTRA_TITLE", title)
            putExtra("EXTRA_TASK_NAME", taskName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode() + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {

        }
    }

    fun cancelTimerAlarm(taskId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode() + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

}