package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingScreen(
    orderId: String,
    onRate: (Float, String) -> Unit
) {
    var rating by remember { mutableStateOf(5f) }
    var comment by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notez votre commande #$orderId", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < rating) Color(0xFFFFC107) else Color.Gray,
                    modifier = Modifier.size(48.dp).padding(4.dp).clickable { rating = index + 1f }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Commentaire (optionnel)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { onRate(rating, comment) }, modifier = Modifier.fillMaxWidth()) {
            Text("Envoyer")
        }
    }
}
