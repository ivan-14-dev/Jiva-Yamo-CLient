package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModal(
    onDismiss: () -> Unit,
    onApply: (List<String>) -> Unit
) {
    val filters = listOf("Livraison Gratuite", "Note 4.0+", "Promotions", "< 30 min", "Sain", "Végétarien")
    val selectedFilters = remember { mutableStateListOf<String>() }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Filtres", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(filters.size) { index ->
                    val filter = filters[index]
                    FilterChip(
                        selected = selectedFilters.contains(filter),
                        onClick = {
                            if (selectedFilters.contains(filter)) selectedFilters.remove(filter)
                            else selectedFilters.add(filter)
                        },
                        label = { Text(filter) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onApply(selectedFilters); onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                Text("Appliquer")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
