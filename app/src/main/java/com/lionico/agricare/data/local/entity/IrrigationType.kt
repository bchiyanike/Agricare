// app/src/main/java/com/lionico/agricare/data/local/entity/IrrigationType.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 10:02 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – irrigation types.
// Impact Radius: FieldEntity.kt (uses this enum)
// Changelog:
// - v1.0: Initial creation – DRIP, SPRINKLER, FLOOD.
// =========================================

package com.lionico.agricare.data.local.entity

enum class IrrigationType {
    DRIP,
    SPRINKLER,
    FLOOD
}