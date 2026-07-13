// app/src/main/java/com/lionico/agricare/LionicoApplication.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-13 10:22 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Notification channels & WorkManager periodic workers.
// Impact Radius: AndroidManifest (permission already added)
// Changelog:
// - v1.1: Created notification channels, enqueued LowStockWorker (daily) and StockCheckReminderWorker (monthly).
// - v1.0: Hilt application shell.
// =========================================

package com.lionico.agricare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lionico.agricare.worker.LowStockWorker
import com.lionico.agricare.worker.StockCheckReminderWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class LionicoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        schedulePeriodicWorkers()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val lowStockChannel = NotificationChannel(
            getString(R.string.notification_channel_low_stock),
            getString(R.string.notification_channel_low_stock_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notification_channel_low_stock_desc)
        }

        val reminderChannel = NotificationChannel(
            getString(R.string.notification_channel_reminders),
            getString(R.string.notification_channel_reminders_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notification_channel_reminders_desc)
        }

        notificationManager.createNotificationChannels(listOf(lowStockChannel, reminderChannel))
    }

    private fun schedulePeriodicWorkers() {
        val lowStockWork = PeriodicWorkRequestBuilder<LowStockWorker>(1, TimeUnit.DAYS)
            .build()

        val stockCheckReminderWork = PeriodicWorkRequestBuilder<StockCheckReminderWorker>(30, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "low_stock_check",
            ExistingPeriodicWorkPolicy.KEEP,
            lowStockWork
        )
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "stock_check_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            stockCheckReminderWork
        )
    }
}