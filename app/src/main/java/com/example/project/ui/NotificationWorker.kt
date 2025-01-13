package com.example.project.ui

import android.Manifest
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.project.MainActivity
import com.example.project.data.impl.ItemsDatabaseRepository

class NotificationWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val repository: ItemsDatabaseRepository

    init {
        // initializing the local repository for the worker
        val appContext = context.applicationContext as Application
        repository = ItemsDatabaseRepository(appContext)
    }
    override suspend fun doWork(): Result {
        // send a notification is the repository is not empty
        if (!repository.getItems().isEmpty()) {
            sendNotification()
        }
        return Result.success()
    }

    private fun sendNotification() {
        // check notification permission and send a new one
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val notificationId = 1
        val notification = createNotification()
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createNotification(): Notification {
        // build and return a new cart notification with a built-in intent
        val channelId = "com.example.project.ui.inventory"
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Cart Reminder")
            .setContentText("You have items in your cart")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())

        return notificationBuilder.build()
    }

    private fun createPendingIntent(): PendingIntent {
        // launch MainActivity as the notification intent
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE here
        )
    }
}

