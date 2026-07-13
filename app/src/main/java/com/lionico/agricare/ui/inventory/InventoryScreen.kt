// app/src/main/java/com/lionico/agricare/ui/inventory/InventoryScreen.kt
// =========================================
// Version: v1.0
// Last Edited: 2026-07-13 10:05 UTC
// Agent: AgriCare Dev Agent
// Active Context: Stage 3 – Inventory UI. Stateless composable with category tabs, list, dialogs.
// Impact Radius: MainActivity.kt (will host), strings.xml (needs strings)
// Changelog:
// - v1.0: Initial creation – tabs, item list, add/edit/delete/stock check.
// =========================================

package com.lionico.agricare.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lionico.agricare.R
import com.lionico.agricare.data.local.entity.InventoryCategory
import com.lionico.agricare.data.local.entity.InventoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    uiState: InventoryUiState,
    onSelectCategory: (InventoryCategory?) -> Unit,
    onAddItem: (String, InventoryCategory, Double, Double, String) -> Unit,
    onUpdateItem: (Long, String, InventoryCategory, Double, Double, String) -> Unit,
    onDeleteItem: (InventoryEntity) -> Unit,
    onStockCheck: (InventoryEntity, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<InventoryEntity?>(null) }
    var stockCheckItem by remember { mutableStateOf<InventoryEntity?>(null) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_item))
            }
        }
    ) { padding ->
        when (uiState) {
            is InventoryUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is InventoryUiState.Error -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is InventoryUiState.Success -> {
                val filteredItems = if (uiState.selectedCategory == null) {
                    uiState.allItems
                } else {
                    uiState.allItems.filter { it.category == uiState.selectedCategory }
                }

                Column(modifier = Modifier.padding(padding)) {
                    // Category tabs
                    ScrollableTabRow(selectedTabIndex = InventoryCategory.values().indexOf(uiState.selectedCategory).coerceAtLeast(0)) {
                        Tab(
                            selected = uiState.selectedCategory == null,
                            onClick = { onSelectCategory(null) },
                            text = { Text(stringResource(R.string.category_all)) }
                        )
                        InventoryCategory.values().forEach { cat ->
                            Tab(
                                selected = uiState.selectedCategory == cat,
                                onClick = { onSelectCategory(cat) },
                                text = { Text(cat.name) }
                            )
                        }
                    }

                    if (filteredItems.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_items_yet))
                        }
                    } else {
                        LazyColumn {
                            items(filteredItems, key = { it.id }) { item ->
                                InventoryItemRow(
                                    item = item,
                                    isLowStock = uiState.lowStockItemIds.contains(item.id),
                                    onEdit = { editingItem = item },
                                    onDelete = { onDeleteItem(item) },
                                    onStockCheck = { stockCheckItem = item }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Dialog
    if (showAddDialog) {
        InventoryFormDialog(
            title = stringResource(R.string.add_item),
            initial = null,
            onConfirm = { name, cat, qty, threshold, unit ->
                onAddItem(name, cat, qty, threshold, unit)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    // Edit Dialog
    editingItem?.let { item ->
        InventoryFormDialog(
            title = stringResource(R.string.edit_item),
            initial = item,
            onConfirm = { name, cat, qty, threshold, unit ->
                onUpdateItem(item.id, name, cat, qty, threshold, unit)
                editingItem = null
            },
            onDismiss = { editingItem = null }
        )
    }

    // Stock Check Dialog
    stockCheckItem?.let { item ->
        StockCheckDialog(
            item = item,
            onConfirm = { actual ->
                onStockCheck(item, actual)
                stockCheckItem = null
            },
            onDismiss = { stockCheckItem = null }
        )
    }
}

@Composable
private fun InventoryItemRow(
    item: InventoryEntity,
    isLowStock: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStockCheck: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        border = if (isLowStock) CardDefaults.outlinedCardBorder().copy(width = 2.dp) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text("${item.bookQuantity} ${item.unit}  |  ${item.category.name}")
                if (isLowStock) {
                    Text(
                        stringResource(R.string.low_stock_warning),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(onClick = onStockCheck) {
                Icon(Icons.Default.Checklist, contentDescription = stringResource(R.string.stock_check))
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
private fun InventoryFormDialog(
    title: String,
    initial: InventoryEntity?,
    onConfirm: (String, InventoryCategory, Double, Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var category by remember { mutableStateOf(initial?.category ?: InventoryCategory.SEED) }
    var bookQuantity by remember { mutableStateOf(initial?.bookQuantity?.toString() ?: "") }
    var minThreshold by remember { mutableStateOf(initial?.minThreshold?.toString() ?: "") }
    var unit by remember { mutableStateOf(initial?.unit ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(name, { name = it }, label = { Text(stringResource(R.string.item_name_label)) })
                CategoryDropdown(
                    label = stringResource(R.string.category_label),
                    selected = category,
                    onSelected = { category = it }
                )
                OutlinedTextField(bookQuantity, { bookQuantity = it }, label = { Text(stringResource(R.string.quantity_label)) })
                OutlinedTextField(minThreshold, { minThreshold = it }, label = { Text(stringResource(R.string.min_threshold_label)) })
                OutlinedTextField(unit, { unit = it }, label = { Text(stringResource(R.string.unit_label)) })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = bookQuantity.toDoubleOrNull() ?: 0.0
                    val thresh = minThreshold.toDoubleOrNull() ?: 0.0
                    onConfirm(name, category, qty, thresh, unit)
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
private fun StockCheckDialog(
    item: InventoryEntity,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var actualQuantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.stock_check_title, item.name)) },
        text = {
            Column {
                Text("${stringResource(R.string.book_quantity)}: ${item.bookQuantity} ${item.unit}")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = actualQuantity,
                    onValueChange = { actualQuantity = it },
                    label = { Text(stringResource(R.string.actual_quantity)) },
                    placeholder = { Text("0") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val actual = actualQuantity.toDoubleOrNull() ?: 0.0
                    onConfirm(actual)
                },
                enabled = actualQuantity.isNotBlank()
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
private fun CategoryDropdown(
    label: String,
    selected: InventoryCategory,
    onSelected: (InventoryCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("$label: $selected")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            InventoryCategory.values().forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat.name) },
                    onClick = { onSelected(cat); expanded = false }
                )
            }
        }
    }
}