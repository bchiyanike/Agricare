// app/src/main/java/com/lionico/agricare/data/local/entity/InventoryEntity.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-13 09:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Added book/physical quantity split, min threshold.
// Impact Radius: InventoryDao, InventoryRepository, UI screens
// Changelog:
// - v1.1: Renamed quantity to bookQuantity, added physicalQuantity (nullable), added minThreshold.
// - v1.0: Initial creation – category, name, quantity, unit.
// =========================================

package com.lionico.agricare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: InventoryCategory,
    val name: String,
    val bookQuantity: Double,
    val physicalQuantity: Double? = null, // null until first stock check
    val minThreshold: Double = 0.0,
    val unit: String
)