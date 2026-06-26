package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.KamerRepository
import com.example.ui.theme.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val history by KamerRepository.searchHistory.collectAsState()
    val currentLang by KamerRepository.currentLanguage.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Rechercher...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (query.isNotEmpty()) {
            // Show suggestions
            Text(
                if (currentLang == "Français") "Suggestions" else "Suggestions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            val suggestions = listOf("Burger", "Pizza", "Ndole", "Bikutsi", "Koki")
                .filter { it.contains(query, ignoreCase = true) }
            
            LazyColumn {
                items(suggestions) { suggestion ->
                    ListItem(
                        headlineContent = { Text(suggestion) },
                        leadingContent = { Icon(Icons.Default.Search, contentDescription = null, tint = UberGrayDark) },
                        modifier = Modifier.clickable { 
                            query = suggestion
                            KamerRepository.addToSearchHistory(suggestion)
                        }
                    )
                }
            }
        } else {
            // Show History
            if (history.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (currentLang == "Français") "Recherches récentes" else "Recent Searches", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { KamerRepository.clearSearchHistory() }) {
                        Text(if (currentLang == "Français") "Effacer" else "Clear", color = UberRed)
                    }
                }
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(history.reversed()) { item ->
                        ListItem(
                            headlineContent = { Text(item) },
                            leadingContent = { Icon(Icons.Default.History, contentDescription = null, tint = UberGrayDark) },
                            modifier = Modifier.clickable { query = item }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Popular Categories
            Text(if (currentLang == "Français") "Catégories populaires" else "Popular Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val popular = listOf("Burger", "Pizza", "Local", "Sain", "Petit-déjeuner", "Africain")
                popular.forEach { category ->
                    SuggestionChip(
                        onClick = { query = category },
                        label = { Text(category) },
                        shape = RoundedCornerShape(20.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(containerColor = UberGrayLight)
                    )
                }
            }
        }
        
        // Add current search to history when it changes (or on "enter")
        LaunchedEffect(query) {
            if (query.isNotEmpty()) {
                // In a real app, maybe do this on submission
            }
        }
    }
}
