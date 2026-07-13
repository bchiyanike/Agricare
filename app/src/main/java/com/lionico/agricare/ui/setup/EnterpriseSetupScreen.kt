// app/src/main/java/com/lionico/agricare/ui/setup/EnterpriseSetupScreen.kt
// =========================================
// Version: v1.2
// Last Edited: 2026-07-13 11:10 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory. Fixing InventoryEntity constructor parameter.
// Impact Radius: None
// Changelog:
// - v1.2: Changed quantity to bookQuantity in InventoryStep to match new entity.
// - v1.1: Replaced single‑field screen with a 4‑step wizard (name, fields, workers, inventory) + summary.
// - v1.0: Initial creation – stateless Compose screen with name input and save.
// =========================================

package com.lionico.agricare.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lionico.agricare.R
import com.lionico.agricare.data.local.entity.*

@Composable
fun EnterpriseSetupScreen(
    uiState: EnterpriseSetupUiState,
    onSetName: (String) -> Unit,
    onAddField: (FieldEntity) -> Unit,
    onRemoveField: (Int) -> Unit,
    onAddWorker: (String) -> Unit,
    onRemoveWorker: (Int) -> Unit,
    onAddInventoryItem: (InventoryEntity) -> Unit,
    onRemoveInventoryItem: (Int) -> Unit,
    onGoToStep: (Int) -> Unit,
    onSkipSection: () -> Unit,
    onCompleteSetup: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is EnterpriseSetupUiState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is EnterpriseSetupUiState.Error -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is EnterpriseSetupUiState.Wizard -> {
            WizardContent(
                state = uiState,
                onSetName = onSetName,
                onAddField = onAddField,
                onRemoveField = onRemoveField,
                onAddWorker = onAddWorker,
                onRemoveWorker = onRemoveWorker,
                onAddInventoryItem = onAddInventoryItem,
                onRemoveInventoryItem = onRemoveInventoryItem,
                onGoToStep = onGoToStep,
                onSkipSection = onSkipSection,
                onCompleteSetup = onCompleteSetup,
                modifier = modifier
            )
        }
        is EnterpriseSetupUiState.Done -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.setup_complete))
            }
        }
    }
}

@Composable
private fun WizardContent(
    state: EnterpriseSetupUiState.Wizard,
    onSetName: (String) -> Unit,
    onAddField: (FieldEntity) -> Unit,
    onRemoveField: (Int) -> Unit,
    onAddWorker: (String) -> Unit,
    onRemoveWorker: (Int) -> Unit,
    onAddInventoryItem: (InventoryEntity) -> Unit,
    onRemoveInventoryItem: (Int) -> Unit,
    onGoToStep: (Int) -> Unit,
    onSkipSection: () -> Unit,
    onCompleteSetup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Text(
            text = stringResource(R.string.step_x_of_5, state.currentStep + 1),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (state.currentStep) {
            0 -> NameStep(name = state.enterpriseName, onNameChanged = onSetName)
            1 -> FieldsStep(
                fields = state.fields,
                onAdd = onAddField,
                onRemove = onRemoveField
            )
            2 -> WorkersStep(
                workers = state.workers,
                onAdd = onAddWorker,
                onRemove = onRemoveWorker
            )
            3 -> InventoryStep(
                items = state.inventoryItems,
                onAdd = onAddInventoryItem,
                onRemove = onRemoveInventoryItem
            )
            4 -> SummaryStep(
                name = state.enterpriseName,
                fieldsCount = state.fields.size,
                workersCount = state.workers.size,
                inventoryCount = state.inventoryItems.size,
                fieldsSkipped = state.fieldsSkipped,
                workersSkipped = state.workersSkipped,
                inventorySkipped = state.inventorySkipped
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.currentStep > 0) {
                OutlinedButton(onClick = { onGoToStep(state.currentStep - 1) }) {
                    Text(stringResource(R.string.back))
                }
            }

            if (state.currentStep in 1..3) {
                TextButton(onClick = onSkipSection) {
                    Text(stringResource(R.string.skip_for_now))
                }
            }

            if (state.currentStep < 4) {
                Button(onClick = { onGoToStep(state.currentStep + 1) }) {
                    Text(stringResource(R.string.next))
                }
            } else {
                Button(
                    onClick = onCompleteSetup,
                    enabled = state.enterpriseName.isNotBlank() && !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(stringResource(R.string.go_to_dashboard))
                }
            }
        }
    }
}

@Composable
private fun NameStep(name: String, onNameChanged: (String) -> Unit) {
    Text(stringResource(R.string.enterprise_setup_title), style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = name,
        onValueChange = onNameChanged,
        label = { Text(stringResource(R.string.enterprise_name_label)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FieldsStep(
    fields: List<FieldEntity>,
    onAdd: (FieldEntity) -> Unit,
    onRemove: (Int) -> Unit
) {
    var fieldName by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var environment by remember { mutableStateOf(EnvironmentType.OPEN_FIELD) }
    var irrigation by remember { mutableStateOf(IrrigationType.DRIP) }

    Text(stringResource(R.string.fields_setup_title), style = MaterialTheme.typography.titleLarge)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(fieldName, { fieldName = it }, label = { Text(stringResource(R.string.field_name_label)) })
    OutlinedTextField(size, { size = it }, label = { Text(stringResource(R.string.field_size_label)) })
    EnumDropdown(
        label = stringResource(R.string.environment_label),
        selected = environment,
        options = EnvironmentType.values().toList(),
        onSelected = { environment = it }
    )
    EnumDropdown(
        label = stringResource(R.string.irrigation_label),
        selected = irrigation,
        options = IrrigationType.values().toList(),
        onSelected = { irrigation = it }
    )
    Button(
        onClick = {
            val ha = size.toDoubleOrNull() ?: 0.0
            onAdd(FieldEntity(name = fieldName, sizeHa = ha, environment = environment, irrigation = irrigation))
            fieldName = ""
            size = ""
        },
        enabled = fieldName.isNotBlank()
    ) {
        Text(stringResource(R.string.add_field))
    }
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn {
        itemsIndexed(fields) { index, field ->
            Text("${field.name} - ${field.sizeHa} ha")
            IconButton(onClick = { onRemove(index) }) {
                Text("X")
            }
        }
    }
}

@Composable
private fun WorkersStep(
    workers: List<WorkerEntity>,
    onAdd: (String) -> Unit,
    onRemove: (Int) -> Unit
) {
    var workerName by remember { mutableStateOf("") }

    Text(stringResource(R.string.workers_setup_title), style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(workerName, { workerName = it }, label = { Text(stringResource(R.string.worker_name_label)) })
    Button(onClick = { onAdd(workerName); workerName = "" }, enabled = workerName.isNotBlank()) {
        Text(stringResource(R.string.add_worker))
    }
    LazyColumn {
        itemsIndexed(workers) { index, worker ->
            Text(worker.name)
            IconButton(onClick = { onRemove(index) }) { Text("X") }
        }
    }
}

@Composable
private fun InventoryStep(
    items: List<InventoryEntity>,
    onAdd: (InventoryEntity) -> Unit,
    onRemove: (Int) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(InventoryCategory.SEED) }

    Text(stringResource(R.string.inventory_setup_title), style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(itemName, { itemName = it }, label = { Text(stringResource(R.string.item_name_label)) })
    OutlinedTextField(quantity, { quantity = it }, label = { Text(stringResource(R.string.quantity_label)) })
    OutlinedTextField(unit, { unit = it }, label = { Text(stringResource(R.string.unit_label)) })
    EnumDropdown(
        label = stringResource(R.string.category_label),
        selected = category,
        options = InventoryCategory.values().toList(),
        onSelected = { category = it }
    )
    Button(
        onClick = {
            val qty = quantity.toDoubleOrNull() ?: 0.0
            onAdd(InventoryEntity(category = category, name = itemName, bookQuantity = qty, unit = unit))
            itemName = ""; quantity = ""; unit = ""
        },
        enabled = itemName.isNotBlank()
    ) {
        Text(stringResource(R.string.add_item))
    }
    LazyColumn {
        itemsIndexed(items) { index, item ->
            Text("${item.name} - ${item.bookQuantity} ${item.unit}")
            IconButton(onClick = { onRemove(index) }) { Text("X") }
        }
    }
}

@Composable
private fun SummaryStep(
    name: String,
    fieldsCount: Int,
    workersCount: Int,
    inventoryCount: Int,
    fieldsSkipped: Boolean,
    workersSkipped: Boolean,
    inventorySkipped: Boolean
) {
    Text(stringResource(R.string.summary_title), style = MaterialTheme.typography.titleLarge)
    Text("Enterprise: $name")
    Text("Fields: $fieldsCount ${if (fieldsSkipped) "(skipped)" else ""}")
    Text("Workers: $workersCount ${if (workersSkipped) "(skipped)" else ""}")
    Text("Inventory items: $inventoryCount ${if (inventorySkipped) "(skipped)" else ""}")
}

@Composable
private fun <T> EnumDropdown(
    label: String,
    selected: T,
    options: List<T>,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selected")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
        }
    }
}