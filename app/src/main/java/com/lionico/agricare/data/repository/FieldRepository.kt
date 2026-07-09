// app/src/main/java/com/lionico/agricare/data/repository/FieldRepository.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-09 12:12 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 2 – Field Management. Exposing update, clearCrop, deleteById.
// Impact Radius: FieldViewModel.kt (uses new methods)
// Changelog:
// - v1.1: Added updateField, clearActiveCrop, deleteFieldById.
// - v1.0: Initial creation – wraps FieldDao with observe, add, delete.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.FieldDao
import com.lionico.agricare.data.local.entity.EnvironmentType
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.IrrigationType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldRepository @Inject constructor(
    private val fieldDao: FieldDao
) {
    fun observeAllFields(): Flow<List<FieldEntity>> = fieldDao.getAllFields()

    suspend fun addField(field: FieldEntity): Long = fieldDao.insertField(field)

    suspend fun deleteFieldById(id: Long) = fieldDao.deleteFieldById(id)

    suspend fun updateField(id: Long, name: String, sizeHa: Double, environment: EnvironmentType, irrigation: IrrigationType) =
        fieldDao.updateField(id, name, sizeHa, environment, irrigation)

    suspend fun clearActiveCrop(id: Long) = fieldDao.clearActiveCrop(id)
}