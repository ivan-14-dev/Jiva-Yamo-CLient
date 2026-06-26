package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Order
import com.example.data.OrderStatus

@Composable
fun TrackingMapScreen(
    order: Order,
    onNavigateBack: () -> Unit
) {
    val steps = listOf(OrderStatus.PENDING, OrderStatus.PREPARING, OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED)
    val currentStep = steps.indexOf(order.status)

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Suivi #${order.id}", style = MaterialTheme.typography.titleLarge)
        }
        
        // Progress Steps
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            steps.forEachIndexed { index, status ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        if (index <= currentStep) Icons.Default.LocationOn else Icons.Default.LocationOn, // Placeholder icons
                        contentDescription = null,
                        tint = if (index <= currentStep) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(status.name.take(4), fontSize = 10.sp)
                }
            }
        }
        
        // Map Placeholder
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) {
            Text("Carte du livreur : ${order.driverLat}, ${order.driverLng}")
        }
        
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Livreur: ${order.driverName ?: "En attente"}", fontWeight = FontWeight.Bold)
                Text("Statut: ${order.status.name}")
            }
        }
    }
}
