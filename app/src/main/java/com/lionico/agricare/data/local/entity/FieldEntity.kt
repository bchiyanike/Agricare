// app/src/main/java/com/lionico/agricare/data/local/entity/FieldEntity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:06 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – field entity.
// Impact Radius: FieldDao.kt, FieldRepository.kt
// Changelog:
// - v1.0: Initial creation – name, size, environment, irrigation, activeCropId nullable.
// =========================================

package com.lionico.agricare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field")
data class FieldEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val sizeHa: Double,          // size in hectares
    val environment: EnvironmentType,
    val irrigation: IrrigationType,
    val activeCropId: Long? = null // null if no active crop
)