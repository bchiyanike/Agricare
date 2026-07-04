// app/src/main/java/com/lionico/agricare/ui/setup/EnterpriseViewModel.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:20 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. ViewModel for enterprise name entry.
// Impact Radius: EnterpriseSetupScreen.kt (will use UiState and events)
// Changelog:
// - v1.0: Initial creation – exposes UiState, handles upsert.
// =========================================

package com.lionico.agricare.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lionico.agricare.data.repository.EnterpriseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EnterpriseUiState {
    data object Loading : EnterpriseUiState
    data class Ready(val savedName: String?) : EnterpriseUiState
    data class Saving(val name: String) : EnterpriseUiState
    data class Error(val message: String) : EnterpriseUiState
}

@HiltViewModel
class EnterpriseViewModel @Inject constructor(
    private val repository: EnterpriseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EnterpriseUiState>(EnterpriseUiState.Loading)
    val uiState: StateFlow<EnterpriseUiState> = _uiState.asStateFlow()

    init {
        observeEnterprise()
    }

    private fun observeEnterprise() {
        viewModelScope.launch {
            repository.observeEnterprise().collect { entity ->
                _uiState.value = if (entity != null) {
                    EnterpriseUiState.Ready(savedName = entity.name)
                } else {
                    EnterpriseUiState.Ready(savedName = null)
                }
            }
        }
    }

    fun saveEnterprise(name: String) {
        if (name.isBlank()) {
            _uiState.update { EnterpriseUiState.Error("Name cannot be empty") }
            return
        }
        viewModelScope.launch {
            _uiState.value = EnterpriseUiState.Saving(name)
            try {
                repository.upsertEnterprise(name)
                // The Flow will automatically update to Ready(savedName = name)
            } catch (e: Exception) {
                _uiState.value = EnterpriseUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
