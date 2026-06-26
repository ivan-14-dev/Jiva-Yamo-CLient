package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(
    onNavigateBack: () -> Unit
) {
    val faqData = listOf(
        FaqCategory(
            "Livraison",
            listOf(
                FaqItem("Où est ma commande ?", "Vous pouvez suivre votre commande en temps réel dans l'onglet 'Suivi'."),
                FaqItem("Ma commande est en retard", "Les délais peuvent varier selon l'affluence. Contactez le support si le retard dépasse 20 min.")
            )
        ),
        FaqCategory(
            "Paiement",
            listOf(
                FaqItem("Moyens de paiement acceptés", "Nous acceptons Orange Money, MTN Mobile Money et le paiement à la livraison."),
                FaqItem("Erreur de paiement", "Vérifiez votre solde ou réessayez avec un autre mode de paiement.")
            )
        ),
        FaqCategory(
            "Compte",
            listOf(
                FaqItem("Changer mon numéro", "Allez dans Profil > Modifier pour mettre à jour vos informations."),
                FaqItem("Supprimer mon compte", "Contactez notre support client pour toute demande de suppression.")
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centre d'Aide / FAQ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = UberWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(faqData) { category ->
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = UberBlack,
                    fontWeight = FontWeight.Black
                )
                category.items.forEach { item ->
                    FaqItemRow(item)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = UberGrayMedium)
            }
        }
    }
}

@Composable
fun FaqItemRow(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.question,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = UberGrayDark
            )
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.answer,
                fontSize = 14.sp,
                color = UberGrayDark,
                lineHeight = 20.sp
            )
        }
    }
}

data class FaqCategory(val title: String, val items: List<FaqItem>)
data class FaqItem(val question: String, val answer: String)
