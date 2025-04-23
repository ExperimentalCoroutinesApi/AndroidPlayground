package com.example.androidplayground.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidplayground.R

/**
 * Common foreground service.
 *
 * Important:
 * 1) Permissions for notifications and foreground services
 * 2) Notification channel
 */
class ForegroundService : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        initNotificationManager()
        initNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_SERVICE_ACTION -> startForegroundService()
            STOP_SERVICE_ACTION -> stopForegroundService()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun initNotificationManager() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun initNotification() {
        val channelId = createChannelForNotification()
        notificationBuilder = NotificationCompat.Builder(this, channelId)
        notificationBuilder
            .setContentTitle("My Foreground Service")
            .setContentText("Sample Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    /**
     * If you target Android 8.0 (API level 26) or higher and post a notification
     * without specifying a notification channel,
     * the notification doesn't appear and the system logs an error.
     *
     * Recreating an existing notification channel with its original values performs no operation,
     * so it's safe to call this code when starting an app.
     */
    private fun createChannelForNotification(): String {
        var channelId = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = CHANNEL_ID
            val channelName = "Sample Foreground Service"
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

    private fun startForegroundService() {
        notification = notificationBuilder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(
                NOTIFICATION_ID,
                notification
            )
        }
    }

    private fun stopForegroundService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        const val START_SERVICE_ACTION = "START_SERVICE"
        const val STOP_SERVICE_ACTION = "STOP_SERVICE"
        const val NOTIFICATION_ID = 66
        const val CHANNEL_ID = "FILET_O_FISH"
    }
}