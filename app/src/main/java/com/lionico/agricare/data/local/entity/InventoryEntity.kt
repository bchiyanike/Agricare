// app/src/main/java/com/lionico/agricare/data/local/entity/InventoryEntity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – inventory entity.
// Impact Radius: InventoryDao.kt, InventoryRepository.kt
// Changelog:
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
    val quantity: Double,
    val unit: String // e.g., "kg", "liters", "pieces"
)