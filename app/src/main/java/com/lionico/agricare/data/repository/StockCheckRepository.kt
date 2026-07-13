// app/src/main/java/com/lionico/agricare/data/repository/StockCheckRepository.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 09:04 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Repository for stock check operations.
// Impact Radius: InventoryViewModel (future use)
// Changelog:
// - v1.0: Wraps StockCheckDao; exposes checks for item, all checks, log new check.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.StockCheckDao
import com.lionico.agricare.data.local.entity.StockCheckEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockCheckRepository @Inject constructor(
    private val stockCheckDao: StockCheckDao
) {
    fun getChecksForItem(itemId: Long): Flow<List<StockCheckEntity>> =
        stockCheckDao.getChecksForItem(itemId)

    fun getAllChecks(): Flow<List<StockCheckEntity>> =
        stockCheckDao.getAllChecks()

    suspend fun logCheck(check: StockCheckEntity) =
        stockCheckDao.insertCheck(check)
}