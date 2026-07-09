// app/src/main/java/com/lionico/agricare/data/local/dao/FieldDao.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-09 12:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 2 – Field Management. Adding update, clearActiveCrop, and deleteById.
// Impact Radius: FieldRepository.kt (new methods)
// Changelog:
// - v1.1: Added updateField, clearActiveCrop, deleteFieldById for field editing and 4‑scenario delete.
// - v1.0: Initial creation – insert, getAll, delete.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.EnvironmentType
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.IrrigationType
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FieldEntity): Long

    @Query("SELECT * FROM field")
    fun getAllFields(): Flow<List<FieldEntity>>

    // replaces the generic delete; now we delete by ID
    @Query("DELETE FROM field WHERE id = :id")
    suspend fun deleteFieldById(id: Long)

    // update field metadata (reset or edit)
    @Query("UPDATE field SET name = :name, sizeHa = :sizeHa, environment = :environment, irrigation = :irrigation WHERE id = :id")
    suspend fun updateField(id: Long, name: String, sizeHa: Double, environment: EnvironmentType, irrigation: IrrigationType)

    // deactivate current crop without deleting field
    @Query("UPDATE field SET activeCropId = NULL WHERE id = :id")
    suspend fun clearActiveCrop(id: Long)
}