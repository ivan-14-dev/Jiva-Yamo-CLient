package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KamerRepository

@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    var selectedPayment by remember { mutableStateOf("Mobile Money") }
    var promoCode by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
            Text("Paiement", style = MaterialTheme.typography.titleLarge)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Adresse de livraison", fontWeight = FontWeight.Bold)
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("Nlongkak, Yaoundé", modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Moyen de paiement", fontWeight = FontWeight.Bold)
        listOf("Mobile Money", "Carte Bancaire").forEach { method ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                RadioButton(selected = selectedPayment == method, onClick = { selectedPayment = method })
                Text(method)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = promoCode,
            onValueChange = { promoCode = it },
            label = { Text("Code Promo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Récapitulatif", fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Sous-total")
            Text("5000 XAF")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Frais")
            Text("500 XAF")
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", fontWeight = FontWeight.Bold)
            Text("5500 XAF", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onOrderPlaced,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Commander")
        }
    }
}
