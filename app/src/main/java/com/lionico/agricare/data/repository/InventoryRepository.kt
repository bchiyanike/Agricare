// app/src/main/java/com/lionico/agricare/data/repository/InventoryRepository.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-13 09:14 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Exposing new DAO methods and low-stock flow.
// Impact Radius: InventoryViewModel (future)
// Changelog:
// - v1.1: Added lowStockItems flow, updateBookQuantity, updatePhysicalQuantity.
// - v1.0: Initial creation – wraps InventoryDao.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.InventoryDao
import com.lionico.agricare.data.local.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val inventoryDao: InventoryDao
) {
    fun observeAllItems(): Flow<List<InventoryEntity>> = inventoryDao.getAllItems()

    fun observeLowStockItems(): Flow<List<InventoryEntity>> = inventoryDao.getLowStockItems()

    suspend fun addItem(item: InventoryEntity): Long = inventoryDao.insertItem(item)

    suspend fun updateBookQuantity(id: Long, quantity: Double) = inventoryDao.updateBookQuantity(id, quantity)

    suspend fun updatePhysicalQuantity(id: Long, quantity: Double) = inventoryDao.updatePhysicalQuantity(id, quantity)

    suspend fun deleteItem(item: InventoryEntity) = inventoryDao.deleteItem(item)
}