// app/src/main/java/com/lionico/agricare/ui/setup/EnterpriseViewModel.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-05 11:20 UTC
// Agent: AgriCare Dev Agent
// Active Context: Hotfix – non‑exhaustive when in skipCurrentSection() causing compile error.
// Impact Radius: None
// Changelog:
// - v1.2: Added missing else branch in skipCurrentSection() to fix compilation.
// - v1.1: Replaced simple name‑only state with full wizard state (step index, fields/workers/inventory lists, skip flags).
// - v1.0: Initial creation – exposes UiState, handles upsert.
// =========================================

package com.lionico.agricare.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lionico.agricare.data.local.entity.*
import com.lionico.agricare.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EnterpriseSetupUiState {
    data object Loading : EnterpriseSetupUiState
    data class Wizard(
        val currentStep: Int,
        val enterpriseName: String,
        val fields: List<FieldEntity>,
        val workers: List<WorkerEntity>,
        val inventoryItems: List<InventoryEntity>,
        val fieldsSkipped: Boolean,
        val workersSkipped: Boolean,
        val inventorySkipped: Boolean,
        val isSaving: Boolean = false
    ) : EnterpriseSetupUiState
    data object Done : EnterpriseSetupUiState
    data class Error(val message: String) : EnterpriseSetupUiState
}

@HiltViewModel
class EnterpriseViewModel @Inject constructor(
    private val enterpriseRepo: EnterpriseRepository,
    private val fieldRepo: FieldRepository,
    private val workerRepo: WorkerRepository,
    private val inventoryRepo: InventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EnterpriseSetupUiState>(EnterpriseSetupUiState.Loading)
    val uiState: StateFlow<EnterpriseSetupUiState> = _uiState.asStateFlow()

    init {
        loadInitialState()
    }

    private fun loadInitialState() {
        viewModelScope.launch {
            val enterprise = enterpriseRepo.observeEnterprise().first()
            if (enterprise != null) {
                _uiState.value = EnterpriseSetupUiState.Done
            } else {
                _uiState.value = EnterpriseSetupUiState.Wizard(
                    currentStep = 0,
                    enterpriseName = "",
                    fields = emptyList(),
                    workers = emptyList(),
                    inventoryItems = emptyList(),
                    fieldsSkipped = false,
                    workersSkipped = false,
                    inventorySkipped = false
                )
            }
        }
    }

    fun setName(name: String) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(enterpriseName = name)
            else old
        }
    }

    fun addField(field: FieldEntity) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(fields = old.fields + field)
            else old
        }
    }

    fun removeField(index: Int) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(fields = old.fields.toMutableList().also { it.removeAt(index) })
            else old
        }
    }

    fun addWorker(name: String) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(workers = old.workers + WorkerEntity(name = name))
            else old
        }
    }

    fun removeWorker(index: Int) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(workers = old.workers.toMutableList().also { it.removeAt(index) })
            else old
        }
    }

    fun addInventoryItem(item: InventoryEntity) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(inventoryItems = old.inventoryItems + item)
            else old
        }
    }

    fun removeInventoryItem(index: Int) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(inventoryItems = old.inventoryItems.toMutableList().also { it.removeAt(index) })
            else old
        }
    }

    fun goToStep(step: Int) {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(currentStep = step)
            else old
        }
    }

    fun skipCurrentSection() {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) {
                when (old.currentStep) {
                    1 -> old.copy(fieldsSkipped = true)
                    2 -> old.copy(workersSkipped = true)
                    3 -> old.copy(inventorySkipped = true)
                    else -> old // steps 0,4 shouldn't be skipped, no change
                }
            } else old
        }
        goToStep(currentStep() + 1)
    }

    fun completeSetup() {
        _uiState.update { old ->
            if (old is EnterpriseSetupUiState.Wizard) old.copy(isSaving = true)
            else old
        }
        viewModelScope.launch {
            try {
                val state = _uiState.value as EnterpriseSetupUiState.Wizard
                enterpriseRepo.upsertEnterprise(
                    name = state.enterpriseName,
                    fieldsComplete = !state.fieldsSkipped && state.fields.isNotEmpty(),
                    workersComplete = !state.workersSkipped && state.workers.isNotEmpty(),
                    inventoryComplete = !state.inventorySkipped && state.inventoryItems.isNotEmpty()
                )
                if (!state.fieldsSkipped) {
                    state.fields.forEach { fieldRepo.addField(it) }
                }
                if (!state.workersSkipped) {
                    state.workers.forEach { workerRepo.addWorker(it) }
                }
                if (!state.inventorySkipped) {
                    state.inventoryItems.forEach { inventoryRepo.addItem(it) }
                }
                _uiState.value = EnterpriseSetupUiState.Done
            } catch (e: Exception) {
                _uiState.value = EnterpriseSetupUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun currentStep(): Int =
        (_uiState.value as? EnterpriseSetupUiState.Wizard)?.currentStep ?: 0
}