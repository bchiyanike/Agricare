// app/src/main/java/com/lionico/agricare/MainActivity.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-05 10:52 UTC
// Agent: AgriCare Dev Agent
// Active Context: Extended enterprise setup – dashboard placeholder with completion flags.
// Impact Radius: None
// Changelog:
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
import com.lionico.agricare.data.repository.EnterpriseRepository
import com.lionico.agricare.ui.setup.EnterpriseSetupScreen
import com.lionico.agricare.ui.setup.EnterpriseSetupUiState
import com.lionico.agricare.ui.theme.LionicoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: EnterpriseRepository

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
                    var showSetup by remember { mutableStateOf<Boolean?>(null) }
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(Unit) {
                        val enterprise = repository.observeEnterprise().first()
                        showSetup = enterprise == null
                    }

                    when (showSetup) {
                        null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        true -> {
                            // Full wizard – EnterpriseSetupScreen will handle its own state
                            EnterpriseSetupScreen(
                                uiState = EnterpriseSetupUiState.Loading, // will be replaced by ViewModel inside
                                onSetName = {},
                                onAddField = {},
                                onRemoveField = {},
                                onAddWorker = {},
                                onRemoveWorker = {},
                                onAddInventoryItem = {},
                                onRemoveInventoryItem = {},
                                onGoToStep = {},
                                onSkipSection = {},
                                onCompleteSetup = {}
                            )
                            // TODO: inject ViewModel instead of dummy callbacks
                        }
                        false -> {
                            // Dashboard placeholder – later check enterprise completion flags
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.app_name))
                            }
                        }
                    }
                }
            }
        }
    }
}