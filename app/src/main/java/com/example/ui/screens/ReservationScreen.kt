package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReservationScreen(
    onReserve: (String, String, Int) -> Unit
) {
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var partySize by remember { mutableStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Réserver une table", style = MaterialTheme.typography.headlineMedium)
        TextField(value = date, onValueChange = { date = it }, label = { Text("Date (JJ/MM/AAAA)") })
        TextField(value = time, onValueChange = { time = it }, label = { Text("Heure (HH:MM)") })
        Text("Nombre de personnes: $partySize")
        Slider(value = partySize.toFloat(), onValueChange = { partySize = it.toInt() }, valueRange = 1f..10f)
        Button(onClick = { onReserve(date, time, partySize) }) {
            Text("Confirmer la réservation")
        }
    }
}
