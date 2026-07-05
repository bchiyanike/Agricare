// app/src/main/java/com/lionico/agricare/data/local/entity/WorkerEntity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:08 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – worker entity.
// Impact Radius: WorkerDao.kt, WorkerRepository.kt
// Changelog:
// - v1.0: Initial creation – simple name list.
// =========================================

package com.lionico.agricare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worker")
data class WorkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)