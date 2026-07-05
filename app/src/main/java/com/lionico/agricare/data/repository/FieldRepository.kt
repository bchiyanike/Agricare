// app/src/main/java/com/lionico/agricare/data/repository/FieldRepository.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:18 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – FieldRepository.
// Impact Radius: EnterpriseSetupViewModel (future use)
// Changelog:
// - v1.0: Initial creation – wraps FieldDao.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.FieldDao
import com.lionico.agricare.data.local.entity.FieldEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldRepository @Inject constructor(
    private val fieldDao: FieldDao
) {
    fun observeAllFields(): Flow<List<FieldEntity>> = fieldDao.getAllFields()

    suspend fun addField(field: FieldEntity): Long = fieldDao.insertField(field)

    suspend fun deleteField(field: FieldEntity) = fieldDao.deleteField(field)
}