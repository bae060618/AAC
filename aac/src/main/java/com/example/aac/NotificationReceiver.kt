package com.example.aac

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, EmotionSelectionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "daily_emotion")
            .setSmallIcon(R.drawable.ic_happy)
            .setContentTitle("오늘의 감정 체크 ")
            .setContentText("오늘 하루 기분은 어땠나요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(context)) {
                notify(1001, builder.build())
            }
        }
    }
}
