package com.glebalekseevjk.floatify_android.presentation.service

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.presentation.activity.MainActivity
import com.glebalekseevjk.floatify_android.presentation.floating.FloatingStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tw.firemaples.onscreenocr.screenshot.ScreenExtractor

class ViewHolderService : Service() {

    companion object {
        private const val ACTION_SHOW_VIEWS = "ACTION_SHOW_VIEWS"
        private const val ACTION_HIDE_VIEWS = "ACTION_HIDE_VIEWS"
        private const val ACTION_EXIT = "ACTION_EXIT"
        private const val ONGOING_NOTIFICATION_ID = 2023
        private const val REQUEST_CODE = 1
        private const val NOTIFICATION_CHANNEL_ID = "floatify_channel_1"

        fun showViews(context: Context) {
            startAction(context, ACTION_SHOW_VIEWS)
        }

        fun hideViews(context: Context) {
            startAction(context, ACTION_HIDE_VIEWS)
        }

        fun exit(context: Context) {
            startAction(context, ACTION_EXIT)
        }

        private fun startAction(context: Context, action: String) {
            context.startService(Intent(context, ViewHolderService::class.java).apply {
                this.action = action
            })
        }
    }
    private var floatingStateListenerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        floatingStateListenerJob = CoroutineScope(Dispatchers.Main).launch {
            FloatingStateHolder.showingStateChangedFlow.collect { startForeground() }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        floatingStateListenerJob?.cancel()
    }
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return when (intent?.action) {
            ACTION_SHOW_VIEWS -> {
                startForeground()
                showViews()
                START_STICKY
            }

            ACTION_HIDE_VIEWS -> {
                hideViews()
                START_STICKY
            }

            ACTION_EXIT -> {
                exit()
                stopForeground()
                START_NOT_STICKY
            }

            else -> {
                START_NOT_STICKY
            }
        }
    }

    private fun exit() {
        hideViews()
        ScreenExtractor.release()
        stopSelf()
    }

    private fun stopForeground() {
        stopForeground(true)
    }

    private fun hideViews() {
//        FloatingStateManager.detachAllViews()
    }

    private fun showViews() {
        if (ScreenExtractor.isGranted) {

            FloatingStateHolder.showMainBar()
        } else {
            startActivity(MainActivity.getLaunchIntent(this))
        }
    }

    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                ONGOING_NOTIFICATION_ID,
                createNotification(true),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION,
            )
        } else {
            startForeground(
                ONGOING_NOTIFICATION_ID,
                createNotification(true),
            )
        }
    }

    private fun createNotification(clickToShow: Boolean): Notification {
        initNotificationChannel()

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setColor(ContextCompat.getColor(this, R.color.purple_500))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background))
            .setTicker(getString(R.string.app_name))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(
                if (clickToShow) getString(R.string.msg_click_to_show_the_floating_bar)
                else getString(R.string.msg_click_to_hide_the_floating_bar)
            )
            .setAutoCancel(false)

        val intent = Intent(this, ViewHolderService::class.java).apply {
            action = if (clickToShow) ACTION_SHOW_VIEWS else ACTION_HIDE_VIEWS
        }
        val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.getService(
                    this, REQUEST_CODE, intent,
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            else
                PendingIntent.getService(
                    this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
                )
        builder.setContentIntent(pendingIntent)

        return builder.build()
    }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = notificationManager.notificationChannels

            channels
                .filter {
                    it.id != NOTIFICATION_CHANNEL_ID && it.id != NotificationChannel.DEFAULT_CHANNEL_ID
                }.forEach {
                    notificationManager.deleteNotificationChannel(it.id)
                }

            if (!channels.any { it.id == NOTIFICATION_CHANNEL_ID }) {
                val channel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    getString(R.string.channel_default),
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    setShowBadge(false)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}