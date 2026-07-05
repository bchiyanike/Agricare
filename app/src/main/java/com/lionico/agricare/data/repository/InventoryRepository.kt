// app/src/main/java/com/lionico/agricare/data/repository/InventoryRepository.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:22 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – InventoryRepository.
// Impact Radius: EnterpriseSetupViewModel (future use)
// Changelog:
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

    suspend fun addItem(item: InventoryEntity): Long = inventoryDao.insertItem(item)

    suspend fun updateQuantity(id: Long, quantity: Double) = inventoryDao.updateQuantity(id, quantity)

    suspend fun deleteItem(item: InventoryEntity) = inventoryDao.deleteItem(item)
}