// app/src/main/java/com/lionico/agricare/MainActivity.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:50 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Replacing template with splash-check and enterprise/dashboard switch.
// Impact Radius: None
// Changelog:
// - v1.0: Removed static TEMPLATE screen; added enterprise existence check via repository and composable switching.
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
import com.lionico.agricare.ui.setup.EnterpriseUiState
import com.lionico.agricare.ui.theme.LionicoTheme
import dagger.hilt.android.AndroidEntryPoint
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
                        val enterprise = repository.getEnterprise()
                        showSetup = enterprise == null
                    }

                    when (showSetup) {
                        null -> {
                            // Splash/loading indicator
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        true -> {
                            EnterpriseSetupScreen(
                                uiState = EnterpriseUiState.Ready(savedName = null),
                                onSaveClick = { name ->
                                    coroutineScope.launch {
                                        repository.upsertEnterprise(name)
                                        showSetup = false
                                    }
                                }
                            )
                        }
                        false -> {
                            // Placeholder dashboard – will be replaced in Stage 6
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
