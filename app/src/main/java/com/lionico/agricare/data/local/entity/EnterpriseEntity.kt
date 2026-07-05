// app/src/main/java/com/lionico/agricare/data/local/entity/EnterpriseEntity.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-05 10:30 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – added wizard‑completion flags.
// Impact Radius: EnterpriseDao (no change needed), EnterpriseRepository (no change), EnterpriseViewModel (uses flags)
// Changelog:
// - v1.1: Added fieldsSetupComplete, workersSetupComplete, inventorySetupComplete booleans.
// - v1.0: Initial creation – EnterpriseEntity with id=1 constraint, name and optional weeklyRoutine.
// =========================================

package com.lionico.agricare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enterprise")
data class EnterpriseEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val weeklyRoutine: String? = null,
    val fieldsSetupComplete: Boolean = false,
    val workersSetupComplete: Boolean = false,
    val inventorySetupComplete: Boolean = false
)