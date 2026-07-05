// app/src/main/java/com/lionico/agricare/data/local/AgricareDatabase.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-05 10:32 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – new entities and DAOs.
// Impact Radius: AppModule.kt (needs new DAO providers)
// Changelog:
// - v1.1: Added FieldEntity, WorkerEntity, InventoryEntity and their DAOs; bumped version to 2.
// - v1.0: Initial creation – database with EnterpriseEntity and EnterpriseDao.
// =========================================

package com.lionico.agricare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.local.dao.FieldDao
import com.lionico.agricare.data.local.dao.WorkerDao
import com.lionico.agricare.data.local.dao.InventoryDao
import com.lionico.agricare.data.local.entity.EnterpriseEntity
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.WorkerEntity
import com.lionico.agricare.data.local.entity.InventoryEntity

@Database(
    entities = [
        EnterpriseEntity::class,
        FieldEntity::class,
        WorkerEntity::class,
        InventoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AgricareDatabase : RoomDatabase() {
    abstract fun enterpriseDao(): EnterpriseDao
    abstract fun fieldDao(): FieldDao
    abstract fun workerDao(): WorkerDao
    abstract fun inventoryDao(): InventoryDao
}