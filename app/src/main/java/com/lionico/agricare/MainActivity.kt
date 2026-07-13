// app/src/main/java/com/lionico/agricare/MainActivity.kt
// =========================================
// Version: v1.5
// Last Edited: 2026-07-13 10:20 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory UI. Added bottom‑bar navigation between Fields and Inventory.
// Impact Radius: None
// Changelog:
// - v1.5: Replaced single FieldScreen with tabbed dashboard (Fields / Inventory).
// - v1.4: Done branch now shows FieldScreen; imported FieldViewModel and FieldScreen.
// - v1.3: Replaced dummy callbacks with EnterpriseViewModel; activity observes ViewModel state.
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
import com.lionico.agricare.ui.field.FieldScreen
import com.lionico.agricare.ui.field.FieldViewModel
import com.lionico.agricare.ui.inventory.InventoryScreen
import com.lionico.agricare.ui.inventory.InventoryViewModel
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
                    val enterpriseViewModel: EnterpriseViewModel = hiltViewModel()
                    val enterpriseState by enterpriseViewModel.uiState.collectAsState()

                    when (enterpriseState) {
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
                                uiState = enterpriseState,
                                onSetName = enterpriseViewModel::setName,
                                onAddField = enterpriseViewModel::addField,
                                onRemoveField = enterpriseViewModel::removeField,
                                onAddWorker = enterpriseViewModel::addWorker,
                                onRemoveWorker = enterpriseViewModel::removeWorker,
                                onAddInventoryItem = enterpriseViewModel::addInventoryItem,
                                onRemoveInventoryItem = enterpriseViewModel::removeInventoryItem,
                                onGoToStep = enterpriseViewModel::goToStep,
                                onSkipSection = enterpriseViewModel::skipCurrentSection,
                                onCompleteSetup = enterpriseViewModel::completeSetup
                            )
                        }
                        is EnterpriseSetupUiState.Done -> {
                            DashboardTabs()
                        }
                        is EnterpriseSetupUiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (enterpriseState as EnterpriseSetupUiState.Error).message,
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

@Composable
private fun DashboardTabs() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val fieldViewModel: FieldViewModel = hiltViewModel()
    val fieldState by fieldViewModel.uiState.collectAsState()
    val inventoryViewModel: InventoryViewModel = hiltViewModel()
    val inventoryState by inventoryViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = { selectedTab = 0 }) {
                    Text(stringResource(R.string.tab_fields))
                }
                TextButton(onClick = { selectedTab = 1 }) {
                    Text(stringResource(R.string.tab_inventory))
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> FieldScreen(
                uiState = fieldState,
                onAddField = fieldViewModel::addField,
                onUpdateField = fieldViewModel::updateField,
                onDeleteField = fieldViewModel::deleteField,
                modifier = Modifier.padding(padding)
            )
            1 -> InventoryScreen(
                uiState = inventoryState,
                onSelectCategory = inventoryViewModel::selectCategory,
                onAddItem = inventoryViewModel::addItem,
                onUpdateItem = inventoryViewModel::updateItem,
                onDeleteItem = inventoryViewModel::deleteItem,
                onStockCheck = inventoryViewModel::performStockCheck,
                modifier = Modifier.padding(padding)
            )
        }
    }
}