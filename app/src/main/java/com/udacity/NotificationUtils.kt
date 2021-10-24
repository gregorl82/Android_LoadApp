package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    status: String
) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.apply {
        putExtra("fileName", messageBody)
        putExtra("status", status)
    }

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val action = NotificationCompat.Action.Builder(
        R.drawable.ic_assistant_black_24dp,
        applicationContext.getString(R.string.notification_button),
        contentPendingIntent
    ).build()

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(action)
        .setContentText("$messageBody has downloaded")
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}