// app/src/main/java/com/lionico/agricare/di/AppModule.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-13 09:18 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Added StockCheckDao provider and HiltWorkerFactory for upcoming workers.
// Impact Radius: None (WorkManager config will be used by future workers)
// Changelog:
// - v1.2: Added StockCheckDao provider; added WorkManager Hilt integration.
// - v1.1: Added FieldDao, WorkerDao, InventoryDao providers; added fallbackToDestructiveMigration.
// - v1.0: Added @Provides for AgricareDatabase and EnterpriseDao.
// =========================================

package com.lionico.agricare.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.Configuration
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

    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(HiltWorkerFactory())
            .build()
    }
}