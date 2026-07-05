// app/src/main/java/com/lionico/agricare/data/repository/WorkerRepository.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:20 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – WorkerRepository.
// Impact Radius: EnterpriseSetupViewModel (future use)
// Changelog:
// - v1.0: Initial creation – wraps WorkerDao.
// =========================================

package com.lionico.agricare.data.repository

import com.lionico.agricare.data.local.dao.WorkerDao
import com.lionico.agricare.data.local.entity.WorkerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerRepository @Inject constructor(
    private val workerDao: WorkerDao
) {
    fun observeAllWorkers(): Flow<List<WorkerEntity>> = workerDao.getAllWorkers()

    suspend fun addWorker(worker: WorkerEntity): Long = workerDao.insertWorker(worker)

    suspend fun deleteWorker(worker: WorkerEntity) = workerDao.deleteWorker(worker)
}