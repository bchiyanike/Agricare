// app/src/main/java/com/lionico/agricare/data/local/AgricareDatabase.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-13 09:16 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Added StockCheckEntity; bumped version to 3.
// Impact Radius: AppModule (new DAO provider)
// Changelog:
// - v1.2: Added StockCheckEntity, StockCheckDao; version 3.
// - v1.1: Added FieldEntity, WorkerEntity, InventoryEntity and their DAOs; version 2.
// - v1.0: Initial creation – database with EnterpriseEntity and EnterpriseDao.
// =========================================

package com.lionico.agricare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.local.dao.FieldDao
import com.lionico.agricare.data.local.dao.WorkerDao
import com.lionico.agricare.data.local.dao.InventoryDao
import com.lionico.agricare.data.local.dao.StockCheckDao
import com.lionico.agricare.data.local.entity.EnterpriseEntity
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.WorkerEntity
import com.lionico.agricare.data.local.entity.InventoryEntity
import com.lionico.agricare.data.local.entity.StockCheckEntity

@Database(
    entities = [
        EnterpriseEntity::class,
        FieldEntity::class,
        WorkerEntity::class,
        InventoryEntity::class,
        StockCheckEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AgricareDatabase : RoomDatabase() {
    abstract fun enterpriseDao(): EnterpriseDao
    abstract fun fieldDao(): FieldDao
    abstract fun workerDao(): WorkerDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun stockCheckDao(): StockCheckDao
}