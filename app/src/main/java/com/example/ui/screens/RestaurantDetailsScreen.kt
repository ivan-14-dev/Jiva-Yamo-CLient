package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.FoodItemCard
import com.example.data.*

@Composable
fun RestaurantDetailsScreen(
    restaurant: Restaurant,
    currentLang: String,
    onNavigateBack: () -> Unit,
    onAddToCart: (CartItem) -> Unit
) {
    var selectedItem by remember { mutableStateOf<MenuItem?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(padding)) {
            Box {
                AsyncImage(
                    model = restaurant.banner,
                    contentDescription = restaurant.name,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(16.dp).background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(restaurant.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕒 30-45 min")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("🛵 Frais: 500 XAF")
                }
                Text("Ouvert: 08:00 - 22:00")
            }
            Divider()
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(restaurant.menu) { item ->
                    FoodItemCard(
                        name = item.name,
                        description = item.description,
                        price = item.price,
                        imageUrl = item.imageUrl,
                        subTitle = null,
                        onAddToCart = { onAddToCart(CartItem(menuItem = item, quantity = 1)) },
                        onClick = { selectedItem = item },
                        currentLang = currentLang
                    )
                }
            }
        }
    }

    // Customization Dialog
    if (selectedItem != null) {
        ProductCustomizationDialog(
            item = selectedItem!!,
            onDismiss = { selectedItem = null },
            onConfirm = { cartItem ->
                onAddToCart(cartItem)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                selectedItem = null
                scope.launch {
                    snackbarHostState.showSnackbar("${cartItem.menuItem?.name ?: "Article"} ajouté au panier")
                }
            }
        )
    }
}

@Composable
fun ProductCustomizationDialog(
    item: MenuItem,
    onDismiss: () -> Unit,
    onConfirm: (CartItem) -> Unit
) {
    var selectedVariant by remember { mutableStateOf(item.variants.firstOrNull()?.name) }
    var selectedSupplements by remember { mutableStateOf(setOf<Supplement>()) }
    var quantity by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(item.name) },
        text = {
            Column {
                if (item.variants.isNotEmpty()) {
                    Text("Variantes:", fontWeight = FontWeight.Bold)
                    item.variants.forEach { variant ->
                        RadioButton(
                            selected = selectedVariant == variant.name,
                            onClick = { selectedVariant = variant.name }
                        )
                        Text(variant.name)
                    }
                }
                if (item.supplements.isNotEmpty()) {
                    Text("Suppléments:", fontWeight = FontWeight.Bold)
                    item.supplements.forEach { supplement ->
                        Checkbox(
                            checked = selectedSupplements.contains(supplement),
                            onCheckedChange = { checked ->
                                if (checked) selectedSupplements += supplement
                                else selectedSupplements -= supplement
                            }
                        )
                        Text(supplement.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(CartItem(menuItem = item, quantity = quantity, selectedVariant = selectedVariant))
            }) {
                Text("Ajouter au panier")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}
