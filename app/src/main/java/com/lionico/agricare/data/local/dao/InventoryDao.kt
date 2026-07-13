// app/src/main/java/com/lionico/agricare/data/local/dao/InventoryDao.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-13 09:12 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Added low-stock query and quantity update methods.
// Impact Radius: InventoryRepository.kt
// Changelog:
// - v1.1: Added getLowStockItems, updateBookQuantity, updatePhysicalQuantity.
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

    @Query("UPDATE inventory SET bookQuantity = :quantity WHERE id = :id")
    suspend fun updateBookQuantity(id: Long, quantity: Double)

    @Query("UPDATE inventory SET physicalQuantity = :quantity WHERE id = :id")
    suspend fun updatePhysicalQuantity(id: Long, quantity: Double)

    @Query("SELECT * FROM inventory WHERE bookQuantity <= minThreshold")
    fun getLowStockItems(): Flow<List<InventoryEntity>>

    @Delete
    suspend fun deleteItem(item: InventoryEntity)
}