// app/src/main/java/com/lionico/agricare/data/local/entity/InventoryCategory.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:04 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – inventory categories.
// Impact Radius: InventoryEntity.kt (uses this enum)
// Changelog:
// - v1.0: Initial creation – SEED, FERTILIZER, PESTICIDE, FUNGICIDE, OTHER.
// =========================================

package com.lionico.agricare.data.local.entity

enum class InventoryCategory {
    SEED,
    FERTILIZER,
    PESTICIDE,
    FUNGICIDE,
    OTHER
}