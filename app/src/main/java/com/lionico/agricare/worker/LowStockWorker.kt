// app/src/main/java/com/lionico/agricare/worker/LowStockWorker.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-13 11:12 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Fixing compilation errors: missing import and ambiguous lambda.
// Impact Radius: None
// Changelog:
// - v1.1: Added kotlinx.coroutines.flow.first import, explicit lambda parameter.
// - v1.0: Initial creation – HiltWorker that queries low stock and posts a notification.
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
import com.lionico.agricare.data.repository.InventoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class LowStockWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val inventoryRepo: InventoryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.success()
        }

        val lowItems = inventoryRepo.observeLowStockItems().first()
        if (lowItems.isEmpty()) return Result.success()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = applicationContext.getString(R.string.notification_channel_low_stock)
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.low_stock_notification_title))
            .setContentText(
                lowItems.joinToString(", ") { item -> "${item.name} (${item.bookQuantity} ${item.unit})" }
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