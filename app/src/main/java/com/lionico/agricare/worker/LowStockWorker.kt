// app/src/main/java/com/lionico/agricare/worker/LowStockWorker.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 10:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Daily low-stock notification worker.
// Impact Radius: LionicoApplication.kt (enqueues periodic work)
// Changelog:
// - v1.0: Initial creation – HiltWorker that queries low stock and posts a notification.
// =========================================

package com.lionico.agricare.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lionico.agricare.R
import com.lionico.agricare.data.repository.InventoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LowStockWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val inventoryRepo: InventoryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.success() // silently skip if permission not granted
        }

        val lowItems = inventoryRepo.observeLowStockItems().first()
        if (lowItems.isEmpty()) return Result.success()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Ensure channel exists (created in LionicoApplication)
        val channelId = applicationContext.getString(R.string.notification_channel_low_stock)
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.low_stock_notification_title))
            .setContentText(
                lowItems.joinToString(", ") { "${it.name} (${it.bookQuantity} ${it.unit})" }
            )
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
            .build()

        notificationManager.notify(LOW_STOCK_NOTIFICATION_ID, notification)
        return Result.success()
    }

    companion object {
        const val LOW_STOCK_NOTIFICATION_ID = 1001
    }
}