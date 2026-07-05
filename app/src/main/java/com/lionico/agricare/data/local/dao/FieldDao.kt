// app/src/main/java/com/lionico/agricare/data/local/dao/FieldDao.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:12 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – FieldDao.
// Impact Radius: AgricareDatabase.kt, FieldRepository.kt
// Changelog:
// - v1.0: Initial creation – insert, getAll, delete.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.FieldEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FieldEntity): Long

    @Query("SELECT * FROM field")
    fun getAllFields(): Flow<List<FieldEntity>>

    @Delete
    suspend fun deleteField(field: FieldEntity)
}