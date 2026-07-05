// app/src/main/java/com/lionico/agricare/MainActivity.kt
// =========================================
// Version: v1.3
// Last Edited: 2026-07-05 11:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – wiring EnterpriseViewModel for real wizard interaction.
// Impact Radius: None
// Changelog:
// - v1.3: Replaced dummy callbacks with EnterpriseViewModel; activity now observes ViewModel state to decide setup vs dashboard.
// - v1.2: Added TODO to pass enterprise flags to dashboard; no functional change.
// - v1.1: Replaced getEnterprise() with observeEnterprise().first().
// - v1.0: Removed static TEMPLATE screen; added enterprise existence check.
// =========================================

package com.lionico.agricare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.lionico.agricare.ui.setup.EnterpriseSetupScreen
import com.lionico.agricare.ui.setup.EnterpriseSetupUiState
import com.lionico.agricare.ui.setup.EnterpriseViewModel
import com.lionico.agricare.ui.theme.LionicoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LionicoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: EnterpriseViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    when (uiState) {
                        is EnterpriseSetupUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is EnterpriseSetupUiState.Wizard -> {
                            EnterpriseSetupScreen(
                                uiState = uiState,
                                onSetName = viewModel::setName,
                                onAddField = viewModel::addField,
                                onRemoveField = viewModel::removeField,
                                onAddWorker = viewModel::addWorker,
                                onRemoveWorker = viewModel::removeWorker,
                                onAddInventoryItem = viewModel::addInventoryItem,
                                onRemoveInventoryItem = viewModel::removeInventoryItem,
                                onGoToStep = viewModel::goToStep,
                                onSkipSection = viewModel::skipCurrentSection,
                                onCompleteSetup = viewModel::completeSetup
                            )
                        }
                        is EnterpriseSetupUiState.Done -> {
                            // Placeholder dashboard – later can show completion status
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.app_name))
                            }
                        }
                        is EnterpriseSetupUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (uiState as EnterpriseSetupUiState.Error).message,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}