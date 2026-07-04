// app/src/main/java/com/lionico/agricare/data/local/dao/EnterpriseDao.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:05 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. DAO for single-row enterprise table.
// Impact Radius: AgricareDatabase.kt (will include this DAO), EnterpriseRepository.kt (will depend on this)
// Changelog:
// - v1.0: Initial creation – upsert and observe enterprise row.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.EnterpriseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EnterpriseDao {

    @Query("SELECT * FROM enterprise LIMIT 1")
    fun observeEnterprise(): Flow<EnterpriseEntity?>

    @Query("SELECT * FROM enterprise LIMIT 1")
    suspend fun getEnterprise(): EnterpriseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEnterprise(enterprise: EnterpriseEntity)
}
