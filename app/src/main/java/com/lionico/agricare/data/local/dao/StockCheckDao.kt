// app/src/main/java/com/lionico/agricare/data/local/dao/StockCheckDao.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 09:02 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. DAO for stock check audit log.
// Impact Radius: StockCheckRepository.kt, AgricareDatabase.kt
// Changelog:
// - v1.0: Insert, get all checks for an item, get all.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.StockCheckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockCheckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheck(check: StockCheckEntity)

    @Query("SELECT * FROM stock_check WHERE inventoryItemId = :itemId ORDER BY checkedDate DESC")
    fun getChecksForItem(itemId: Long): Flow<List<StockCheckEntity>>

    @Query("SELECT * FROM stock_check ORDER BY checkedDate DESC")
    fun getAllChecks(): Flow<List<StockCheckEntity>>
}