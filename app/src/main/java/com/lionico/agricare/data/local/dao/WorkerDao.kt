// app/src/main/java/com/lionico/agricare/data/local/dao/WorkerDao.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:14 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – WorkerDao.
// Impact Radius: AgricareDatabase.kt, WorkerRepository.kt
// Changelog:
// - v1.0: Initial creation – insert, getAll, delete.
// =========================================

package com.lionico.agricare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lionico.agricare.data.local.entity.WorkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorker(worker: WorkerEntity): Long

    @Query("SELECT * FROM worker")
    fun getAllWorkers(): Flow<List<WorkerEntity>>

    @Delete
    suspend fun deleteWorker(worker: WorkerEntity)
}