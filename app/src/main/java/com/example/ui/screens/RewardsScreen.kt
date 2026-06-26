package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.*

@Composable
fun RewardsScreen(
    userProfile: UserProfile
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Programme de Fidélité", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Niveau actuel: ${userProfile.loyaltyLevel}")
                Text("Points: ${userProfile.points}")
                Text("Cashback disponible: ${userProfile.cashback} XAF")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Parrainage", style = MaterialTheme.typography.headlineSmall)
        Text("Code: ${userProfile.parrainageCode}")
    }
}
