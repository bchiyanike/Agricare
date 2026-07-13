// app/src/main/java/com/lionico/agricare/data/local/Converters.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 10:35 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Room type converter for LocalDate.
// Impact Radius: AgricareDatabase.kt (will use this converter)
// Changelog:
// - v1.0: Initial creation – LocalDate <-> Long epoch day converter.
// =========================================

package com.lionico.agricare.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }
}