// app/src/main/java/com/lionico/agricare/data/local/entity/StockCheckEntity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 09:00 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Entity for stock reconciliation audit log.
// Impact Radius: StockCheckDao.kt, StockCheckRepository.kt, AgricareDatabase.kt
// Changelog:
// - v1.0: Initial creation – book vs physical quantity, date, difference.
// =========================================

package com.lionico.agricare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "stock_check")
data class StockCheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val inventoryItemId: Long,
    val checkedDate: LocalDate,
    val bookQuantity: Double,
    val actualQuantity: Double,
    val difference: Double
)