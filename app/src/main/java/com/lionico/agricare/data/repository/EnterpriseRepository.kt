// app/src/main/java/com/lionico/agricare/data/repository/EnterpriseRepository.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-05 10:40 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – upsert now accepts setup flags.
// Impact Radius: EnterpriseViewModel (uses the new upsert overload)
// Changelog:
// - v1.1: Added upsertEnterprise overload with setup‑completion booleans.
// - v1.0: Initial creation – exposes observe and upsert for the enterprise.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.local.entity.EnterpriseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnterpriseRepository @Inject constructor(
    private val enterpriseDao: EnterpriseDao
) {
    fun observeEnterprise(): Flow<EnterpriseEntity?> = enterpriseDao.observeEnterprise()

    suspend fun upsertEnterprise(name: String) {
        enterpriseDao.upsertEnterprise(EnterpriseEntity(name = name))
    }

    suspend fun upsertEnterprise(
        name: String,
        fieldsComplete: Boolean,
        workersComplete: Boolean,
        inventoryComplete: Boolean
    ) {
        enterpriseDao.upsertEnterprise(
            EnterpriseEntity(
                name = name,
                fieldsSetupComplete = fieldsComplete,
                workersSetupComplete = workersComplete,
                inventorySetupComplete = inventoryComplete
            )
        )
    }
}