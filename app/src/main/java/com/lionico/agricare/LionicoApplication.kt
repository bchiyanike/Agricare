// app/src/main/java/com/lionico/agricare/LionicoApplication.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-13 11:08 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Fixing HiltWorkerFactory integration.
// Impact Radius: None (configuration change)
// Changelog:
// - v1.2: Implemented Configuration.Provider to use HiltWorkerFactory.
// - v1.1: Created notification channels, enqueued periodic workers.
// - v1.0: Hilt application shell.
// =========================================

package com.lionico.agricare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lionico.agricare.worker.LowStockWorker
import com.lionico.agricare.worker.StockCheckReminderWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class LionicoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

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