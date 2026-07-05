// app/src/main/java/com/lionico/agricare/data/local/dao/InventoryDao.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:16 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – InventoryDao.
// Impact Radius: AgricareDatabase.kt, InventoryRepository.kt
// Changelog:
// - v1.0: Initial creation – insert, getAll, delete, updateQuantity.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryEntity): Long

    @Query("SELECT * FROM inventory")
    fun getAllItems(): Flow<List<InventoryEntity>>

    @Query("UPDATE inventory SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Double)

    @Delete
    suspend fun deleteItem(item: InventoryEntity)
}