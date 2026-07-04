// app/src/main/java/com/lionico/agricare/data/local/entity/EnterpriseEntity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:00 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Creating the single-row entity that holds enterprise metadata.
// Impact Radius: EnterpriseDao.kt (needs this entity), AgricareDatabase.kt (will include this entity)
// Changelog:
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
    val weeklyRoutine: String? = null
)
