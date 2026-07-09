// app/src/main/java/com/lionico/agricare/ui/field/FieldViewModel.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-05 12:00 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 2 – Field Management. ViewModel with CRUD and 4‑option delete.
// Impact Radius: FieldScreen.kt (consumes state and events)
// Changelog:
// - v1.0: Initial creation – load, add, update, and 4‑scenario delete.
// =========================================

package com.lionico.agricare.ui.field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lionico.agricare.data.local.entity.EnvironmentType
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.IrrigationType
import com.lionico.agricare.data.repository.FieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface FieldUiState {
    data object Loading : FieldUiState
    data class Success(val fields: List<FieldEntity>) : FieldUiState
    data class Error(val message: String) : FieldUiState
}

sealed interface FieldDeleteAction {
    data object ResetData : FieldDeleteAction
    data object DeactivateCropKeepHistory : FieldDeleteAction
    data object DeactivateCropDeleteHistory : FieldDeleteAction
    data object DeleteField : FieldDeleteAction
}

@HiltViewModel
class FieldViewModel @Inject constructor(
    private val fieldRepo: FieldRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FieldUiState>(FieldUiState.Loading)
    val uiState: StateFlow<FieldUiState> = _uiState.asStateFlow()

    init {
        loadFields()
    }

    private fun loadFields() {
        viewModelScope.launch {
            fieldRepo.observeAllFields().collect { fields ->
                _uiState.value = FieldUiState.Success(fields)
            }
        }
    }

    fun addField(name: String, sizeHa: Double, environment: EnvironmentType, irrigation: IrrigationType) {
        viewModelScope.launch {
            try {
                fieldRepo.addField(
                    FieldEntity(
                        name = name,
                        sizeHa = sizeHa,
                        environment = environment,
                        irrigation = irrigation
                    )
                )
            } catch (e: Exception) {
                _uiState.value = FieldUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateField(id: Long, name: String, sizeHa: Double, environment: EnvironmentType, irrigation: IrrigationType) {
        viewModelScope.launch {
            try {
                fieldRepo.updateField(id, name, sizeHa, environment, irrigation)
            } catch (e: Exception) {
                _uiState.value = FieldUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteField(field: FieldEntity, action: FieldDeleteAction) {
        viewModelScope.launch {
            try {
                when (action) {
                    FieldDeleteAction.ResetData -> {
                        fieldRepo.updateField(
                            field.id,
                            name = "",       // clear name
                            sizeHa = 0.0,    // reset size
                            environment = EnvironmentType.OPEN_FIELD,
                            irrigation = IrrigationType.DRIP
                        )
                    }
                    FieldDeleteAction.DeactivateCropKeepHistory -> {
                        if (field.activeCropId != null) {
                            fieldRepo.clearActiveCrop(field.id)
                            // TODO Stage 4: mark crop isActive = false, keep history
                        }
                    }
                    FieldDeleteAction.DeactivateCropDeleteHistory -> {
                        if (field.activeCropId != null) {
                            fieldRepo.clearActiveCrop(field.id)
                            // TODO Stage 4: mark crop isActive = false + delete history
                        }
                    }
                    FieldDeleteAction.DeleteField -> {
                        // If field has active crop, deactivate first
                        if (field.activeCropId != null) {
                            fieldRepo.clearActiveCrop(field.id)
                            // TODO Stage 4: also handle crop deactivation and history
                        }
                        fieldRepo.deleteFieldById(field.id)
                    }
                }
                // Flow will automatically emit updated list
            } catch (e: Exception) {
                _uiState.value = FieldUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}