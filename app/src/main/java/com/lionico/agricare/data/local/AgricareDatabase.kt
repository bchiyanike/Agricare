// app/src/main/java/com/lionico/agricare/data/local/AgricareDatabase.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Room database class with enterprise DAO.
// Impact Radius: AppModule.kt (needs database provider), EnterpriseRepository.kt (injects DAO)
// Changelog:
// - v1.0: Initial creation – database with EnterpriseEntity and EnterpriseDao.
// =========================================

package com.lionico.agricare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.local.entity.EnterpriseEntity

@Database(
    entities = [EnterpriseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AgricareDatabase : RoomDatabase() {
    abstract fun enterpriseDao(): EnterpriseDao
}
