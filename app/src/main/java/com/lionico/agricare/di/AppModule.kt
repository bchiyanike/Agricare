// app/src/main/java/com/lionico/agricare/di/AppModule.kt
// =========================================
// Version: v1.3
// Last Edited: 2026-07-13 11:05 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Fixing HiltWorkerFactory integration.
// Impact Radius: LionicoApplication.kt (now handles WorkManager config)
// Changelog:
// - v1.3: Removed invalid provideWorkManagerConfiguration(); HiltWorkerFactory will be provided by Application.
// - v1.2: Added StockCheckDao provider; added WorkManager Hilt integration (now removed).
// - v1.1: Added FieldDao, WorkerDao, InventoryDao providers; added fallbackToDestructiveMigration.
// - v1.0: Added @Provides for AgricareDatabase and EnterpriseDao.
// =========================================

package com.lionico.agricare.di

import android.content.Context
import androidx.room.Room
import com.lionico.agricare.data.local.AgricareDatabase
import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.local.dao.FieldDao
import com.lionico.agricare.data.local.dao.WorkerDao
import com.lionico.agricare.data.local.dao.InventoryDao
import com.lionico.agricare.data.local.dao.StockCheckDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AgricareDatabase {
        return Room.databaseBuilder(
            context,
            AgricareDatabase::class.java,
            "agricare_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideEnterpriseDao(database: AgricareDatabase): EnterpriseDao = database.enterpriseDao()

    @Provides
    fun provideFieldDao(database: AgricareDatabase): FieldDao = database.fieldDao()

    @Provides
    fun provideWorkerDao(database: AgricareDatabase): WorkerDao = database.workerDao()

    @Provides
    fun provideInventoryDao(database: AgricareDatabase): InventoryDao = database.inventoryDao()

    @Provides
    fun provideStockCheckDao(database: AgricareDatabase): StockCheckDao = database.stockCheckDao()
}