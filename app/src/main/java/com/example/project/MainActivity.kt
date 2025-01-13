package com.example.project

import com.example.project.ui.NotificationWorker
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project.ui.MainScreen
import android.Manifest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.project.ui.theme.CustomTheme
import java.time.Duration


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalComposeUiApi::class)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission
        requestNotificationPermission()

        setContent {
            CustomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomTheme.colorScheme.background
                ) {
                    MainScreen() // opening the main screen
                }
            }
        }
        // begin the worker scheduler to send notifications every 1.5 hours
        // if the cart is not empty
        scheduleNotificationWork()
    }


    // function to request notification permission when app is first opened
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    // storing the permission codes for the requests
    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }

    // function to schedule the cart notification periodically
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotificationWork() {
        // notification worker scheduler
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(Duration.ofMinutes(90))
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(
                workRequest
            )
    }

}