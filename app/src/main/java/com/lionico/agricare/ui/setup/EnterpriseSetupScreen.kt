// app/src/main/java/com/lionico/agricare/ui/setup/EnterpriseSetupScreen.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-03 17:35 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 1 – Enterprise Setup. Screen for entering enterprise name.
// Impact Radius: strings.xml (needs new string resources for labels and hints).
// Changelog:
// - v1.0: Initial creation – stateless Compose screen with name input and save.
// =========================================

package com.lionico.agricare.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lionico.agricare.R

@Composable
fun EnterpriseSetupScreen(
    uiState: EnterpriseUiState,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }

    // Pre‑fill name if we already have a saved enterprise (useful for editing, though we direct to dashboard)
    LaunchedEffect(uiState) {
        if (uiState is EnterpriseUiState.Ready && uiState.savedName != null) {
            name = uiState.savedName
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.enterprise_setup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.enterprise_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState is EnterpriseUiState.Error,
            supportingText = {
                if (uiState is EnterpriseUiState.Error) {
                    Text(stringResource(R.string.error_empty_name))
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSaveClick(name) },
            enabled = uiState !is EnterpriseUiState.Saving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is EnterpriseUiState.Saving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(stringResource(R.string.save))
        }
    }
}
