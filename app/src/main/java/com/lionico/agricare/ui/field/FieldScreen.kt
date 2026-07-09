// app/src/main/java/com/lionico/agricare/ui/field/FieldScreen.kt
// =========================================
// Version: v1.1
// Last Edited: 2026-07-09 12:25 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 2 – Field Management. Hotfix type inference error for selectedDeleteAction.
// Impact Radius: None
// Changelog:
// - v1.1: Fixed type inference in selectedDeleteAction to allow any FieldDeleteAction.
// - v1.0: Initial creation – field list, add dialog, 4‑option delete dialog, edit support.
// =========================================

package com.lionico.agricare.ui.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lionico.agricare.R
import com.lionico.agricare.data.local.entity.EnvironmentType
import com.lionico.agricare.data.local.entity.FieldEntity
import com.lionico.agricare.data.local.entity.IrrigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldScreen(
    uiState: FieldUiState,
    onAddField: (String, Double, EnvironmentType, IrrigationType) -> Unit,
    onUpdateField: (Long, String, Double, EnvironmentType, IrrigationType) -> Unit,
    onDeleteField: (FieldEntity, FieldDeleteAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf<FieldEntity?>(null) }
    var deletingField by remember { mutableStateOf<FieldEntity?>(null) }
    var selectedDeleteAction by remember { mutableStateOf<FieldDeleteAction>(FieldDeleteAction.ResetData) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_field))
            }
        }
    ) { padding ->
        when (uiState) {
            is FieldUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is FieldUiState.Error -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is FieldUiState.Success -> {
                if (uiState.fields.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_fields_yet))
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(padding)) {
                        items(uiState.fields, key = { it.id }) { field ->
                            FieldRow(
                                field = field,
                                onEdit = { editingField = field },
                                onDelete = {
                                    deletingField = field
                                    selectedDeleteAction = FieldDeleteAction.ResetData
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        FieldFormDialog(
            title = stringResource(R.string.add_field),
            initial = null,
            onConfirm = { name, size, env, irr ->
                onAddField(name, size, env, irr)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    // Edit Dialog
    editingField?.let { field ->
        FieldFormDialog(
            title = stringResource(R.string.edit_field),
            initial = field,
            onConfirm = { name, size, env, irr ->
                onUpdateField(field.id, name, size, env, irr)
                editingField = null
            },
            onDismiss = { editingField = null }
        )
    }

    // Delete Dialog
    deletingField?.let { field ->
        DeleteFieldDialog(
            fieldName = field.name,
            selectedAction = selectedDeleteAction,
            onActionSelected = { selectedDeleteAction = it },
            onConfirm = {
                onDeleteField(field, selectedDeleteAction)
                deletingField = null
            },
            onDismiss = { deletingField = null }
        )
    }
}

@Composable
private fun FieldRow(
    field: FieldEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(field.name, style = MaterialTheme.typography.titleMedium)
                Text("${field.sizeHa} ha | ${field.environment} | ${field.irrigation}")
                if (field.activeCropId != null) {
                    Text(
                        stringResource(R.string.active_crop, field.activeCropId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
            }
        }
    }
}

@Composable
private fun FieldFormDialog(
    title: String,
    initial: FieldEntity?,
    onConfirm: (String, Double, EnvironmentType, IrrigationType) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var size by remember { mutableStateOf(initial?.sizeHa?.toString() ?: "") }
    var environment by remember { mutableStateOf(initial?.environment ?: EnvironmentType.OPEN_FIELD) }
    var irrigation by remember { mutableStateOf(initial?.irrigation ?: IrrigationType.DRIP) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(name, { name = it }, label = { Text(stringResource(R.string.field_name_label)) })
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val ha = size.toDoubleOrNull() ?: 0.0
                    onConfirm(name, ha, environment, irrigation)
                },
                enabled = name.isNotBlank()
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
fun DeleteFieldDialog(
    fieldName: String,
    selectedAction: FieldDeleteAction,
    onActionSelected: (FieldDeleteAction) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_field_title, fieldName)) },
        text = {
            Column {
                RadioButtonRow(
                    label = stringResource(R.string.delete_reset_data),
                    selected = selectedAction == FieldDeleteAction.ResetData,
                    onClick = { onActionSelected(FieldDeleteAction.ResetData) }
                )
                RadioButtonRow(
                    label = stringResource(R.string.delete_deactivate_crop_keep_history),
                    selected = selectedAction == FieldDeleteAction.DeactivateCropKeepHistory,
                    onClick = { onActionSelected(FieldDeleteAction.DeactivateCropKeepHistory) }
                )
                RadioButtonRow(
                    label = stringResource(R.string.delete_deactivate_crop_delete_history),
                    selected = selectedAction == FieldDeleteAction.DeactivateCropDeleteHistory,
                    onClick = { onActionSelected(FieldDeleteAction.DeactivateCropDeleteHistory) }
                )
                RadioButtonRow(
                    label = stringResource(R.string.delete_field_entirely),
                    selected = selectedAction == FieldDeleteAction.DeleteField,
                    onClick = { onActionSelected(FieldDeleteAction.DeleteField) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
private fun RadioButtonRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
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