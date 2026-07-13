// app/src/main/java/com/lionico/agricare/worker/StockCheckReminderWorker.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 10:12 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Monthly stock-check reminder worker.
// Impact Radius: LionicoApplication.kt (enqueues periodic work)
// Changelog:
// - v1.0: Initial creation – periodic notification for monthly stock check.
// =========================================

package com.lionico.agricare.worker

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lionico.agricare.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class StockCheckReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.success()
        }

        val channelId = applicationContext.getString(R.string.notification_channel_reminders)
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.stock_check_reminder_title))
            .setContentText(applicationContext.getString(R.string.stock_check_reminder_body))
            .setAutoCancel(true)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(STOCK_CHECK_REMINDER_ID, notification)
        return Result.success()
    }

    companion object {
        const val STOCK_CHECK_REMINDER_ID = 1002
    }
}