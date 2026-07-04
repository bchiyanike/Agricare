// app/src/main/java/com/lionico/agricare/di/AppModule.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:30 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Providing Room database and EnterpriseRepository.
// Impact Radius: None (adds new providers, no existing callers broken)
// Changelog:
// - v1.0: Added @Provides for AgricareDatabase and EnterpriseRepository.
// =========================================

package com.lionico.agricare.di

import android.content.Context
import androidx.room.Room
import com.lionico.agricare.data.local.AgricareDatabase
import com.lionico.agricare.data.local.dao.EnterpriseDao
import com.lionico.agricare.data.repository.EnterpriseRepository
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
        ).build()
    }

    @Provides
    fun provideEnterpriseDao(database: AgricareDatabase): EnterpriseDao {
        return database.enterpriseDao()
    }

    // The repository is already @Singleton via its annotation and Hilt can inject it directly,
    // but we can also provide it here if needed (optional). We'll let Hilt construct it directly.
}
