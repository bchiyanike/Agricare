// app/src/main/java/com/lionico/agricare/data/repository/EnterpriseRepository.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:15 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Repository wrapping EnterpriseDao.
// Impact Radius: EnterpriseViewModel.kt (will depend on this)
// Changelog:
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
        enterpriseDao.upsertEnterprise(
            EnterpriseEntity(name = name)
        )
    }
}
