package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CartItem
import com.example.data.KamerRepository
import com.example.data.MenuItem
import com.example.data.PartnerRequest
import com.example.data.PartnerType
import com.example.data.Product
import com.example.data.RequestStatus
import com.example.data.Restaurant
import com.example.data.Seller
import com.example.data.SellerType
import com.example.data.UserRole
import com.example.ui.theme.AccentYellow
import com.example.ui.theme.CameroonRed
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryGreen
import com.example.ui.components.FoodItemCard
import com.example.ui.components.RestaurantCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VisitorScreen(
    onNavigateToAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSubScreen by remember { mutableStateOf("home") } // home, rest_detail, seller_detail, product_detail, partner, info, verify
    var selectedRestId by remember { mutableStateOf("") }
    var selectedSellerId by remember { mutableStateOf("") }
    var selectedProductId by remember { mutableStateOf("") }
    var selectedProductSellerId by remember { mutableStateOf("") }

    val currentLang by KamerRepository.currentLanguage.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Quick Tab Switcher inside the Visitor view
        TabRow(
            selectedTabIndex = when (currentSubScreen) {
                "home" -> 0
                "info" -> 1
                else -> 0
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SecondaryGreen
        ) {
            Tab(
                selected = currentSubScreen == "home" || currentSubScreen.contains("detail"),
                onClick = { currentSubScreen = "home" },
                text = { Text(if (currentLang == "Français") "Accueil" else "Home") }
            )
            Tab(
                selected = currentSubScreen == "info",
                onClick = { currentSubScreen = "info" },
                text = { Text(if (currentLang == "Français") "À propos & FAQ" else "About & FAQ") }
            )
        }

        when (currentSubScreen) {
            "home" -> VisitorHome(
                currentLang = currentLang,
                onRestClick = { id ->
                    selectedRestId = id
                    currentSubScreen = "rest_detail"
                },
                onSellerClick = { id ->
                    selectedSellerId = id
                    currentSubScreen = "seller_detail"
                },
                onProductClick = { prodId, sellId ->
                    selectedProductId = prodId
                    selectedProductSellerId = sellId
                    currentSubScreen = "product_detail"
                }
            )
            "rest_detail" -> RestaurantDetail(
                restId = selectedRestId,
                currentLang = currentLang,
                onBack = { currentSubScreen = "home" },
                onAuthRedirect = onNavigateToAuth
            )
            "seller_detail" -> SellerDetail(
                sellerId = selectedSellerId,
                currentLang = currentLang,
                onBack = { currentSubScreen = "home" },
                onProductClick = { prodId ->
                    selectedProductId = prodId
                    selectedProductSellerId = selectedSellerId
                    currentSubScreen = "product_detail"
                },
                onAuthRedirect = onNavigateToAuth
            )
            "product_detail" -> ProductDetail(
                productId = selectedProductId,
                sellerId = selectedProductSellerId,
                currentLang = currentLang,
                onBack = { currentSubScreen = "seller_detail" },
                onAuthRedirect = onNavigateToAuth
            )
            "info" -> InfoSection(currentLang = currentLang)
            else -> {
                currentSubScreen = "home"
            }
        }
    }
}

@Composable
fun VisitorHome(
    currentLang: String,
    onRestClick: (String) -> Unit,
    onSellerClick: (String) -> Unit,
    onProductClick: (String, String) -> Unit
) {
    val restaurants by KamerRepository.restaurants.collectAsState()
    val sellers by KamerRepository.sellers.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("Yaoundé") }
    var selectedFoodCategory by remember { mutableStateOf("Tout") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        // High-fidelity local banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800",
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(CameroonRed, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == "Français") "PROMO DU JOUR : -20% code MBOA20" else "DAILY DEALS: 20% off code MBOA20",
                        color = AccentYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (currentLang == "Français") "Commandez dans vos villes favorites" else "Order from your favorite cities",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (currentLang == "Français") "Rechercher Ndolé, pizza, supermarché..." else "Search Ndole, pizza, grocery...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SecondaryGreen) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SecondaryGreen,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("visitor_search_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cities horizontal list
        Text(
            text = if (currentLang == "Français") "📍 Villes desservies" else "📍 Covered Cities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(KamerRepository.cities) { city ->
                val isSelected = selectedCity == city
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) SecondaryGreen else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedCity = city }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = city,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Categories filters
        Text(
            text = if (currentLang == "Français") "🍔 Catégories Populaires" else "🍔 Popular Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(KamerRepository.foodCategories) { cat ->
                val isSelected = selectedFoodCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) PrimaryOrange else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedFoodCategory = cat }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Restaurants section
        Text(
            text = if (currentLang == "Français") "🍽 Restaurants Partenaires" else "🍽 Partner Restaurants",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        val filteredRest = restaurants.filter {
            it.city == selectedCity &&
            (selectedFoodCategory == "Tout" || it.categories.contains(selectedFoodCategory)) &&
            (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true))
        }

        if (filteredRest.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentLang == "Français") "Aucun restaurant trouvé à $selectedCity" else "No restaurants found in $selectedCity",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                filteredRest.forEach { rest ->
                    RestaurantCard(
                        restaurant = rest,
                        onClick = { onRestClick(rest.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sellers section
        Text(
            text = if (currentLang == "Français") "🛒 Marchés & Boutiques Alimentaires" else "🛒 Markets & Food Shops",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        val filteredSellers = sellers.filter {
            it.city == selectedCity &&
            (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true))
        }

        if (filteredSellers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentLang == "Français") "Aucune boutique trouvée à $selectedCity" else "No shops found in $selectedCity",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                filteredSellers.forEach { seller ->
                    RestaurantCard(
                        restaurant = Restaurant(
                            id = seller.id,
                            name = seller.name,
                            description = seller.description,
                            logo = seller.logo,
                            banner = seller.banner,
                            address = seller.address,
                            city = seller.city,
                            rating = seller.rating,
                            isSponsored = false, // Seller doesn't have isSponsored in model
                            categories = listOf(seller.type.name)
                        ),
                        onClick = { onSellerClick(seller.id) },
                        currentLang = currentLang
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantDetail(
    restId: String,
    currentLang: String,
    onBack: () -> Unit,
    onAuthRedirect: () -> Unit
) {
    val restaurants by KamerRepository.restaurants.collectAsState()
    val rest = restaurants.find { it.id == restId } ?: return

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Banner with Back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            AsyncImage(
                model = rest.banner,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                shape = CircleShape,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            ) {
                Text("←", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Header Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(rest.logo, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(rest.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = AccentYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${rest.rating} (${rest.reviews.size} avis)", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(rest.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📍 Adresse: ${rest.address}, ${rest.city}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryGreen)
            Text("🕒 Horaires d'ouverture: ${rest.hours}", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(20.dp))

            // Non accessible button wrapper (MANDATORY prompt to connect!)
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryOrange.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (currentLang == "Français") "Vous parcourez le menu librement sans connexion." else "You are browsing the menu freely as guest.",
                        fontSize = 13.sp,
                        color = PrimaryOrange,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onAuthRedirect,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("connect_to_order_button")
                    ) {
                        Text(
                            text = if (currentLang == "Français") "Se connecter pour commander" else "Login to order menu items",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Items List
            Text(
                text = if (currentLang == "Français") "📋 Notre Carte / Menu" else "📋 Menu Items",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            rest.menu.forEach { item ->
                FoodItemCard(
                    name = item.name,
                    description = item.description,
                    price = item.price,
                    imageUrl = item.imageUrl,
                    subTitle = null,
                    onAddToCart = { onAuthRedirect() },
                    onClick = { onAuthRedirect() },
                    currentLang = currentLang
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reviews List
            Text(
                text = if (currentLang == "Français") "⭐ Avis de nos clients" else "⭐ Customer Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            rest.reviews.forEach { rev ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(rev.author, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < rev.rating) AccentYellow else Color.LightGray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(rev.comment, fontSize = 12.sp)
                        rev.reply?.let { r ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SecondaryGreen.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Text("Réponse du resto: $r", fontSize = 11.sp, color = SecondaryGreen, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SellerDetail(
    sellerId: String,
    currentLang: String,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit,
    onAuthRedirect: () -> Unit
) {
    val sellers by KamerRepository.sellers.collectAsState()
    val seller = sellers.find { it.id == sellerId } ?: return

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            AsyncImage(
                model = seller.banner,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                shape = CircleShape,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            ) {
                Text("←", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${seller.logo} ${seller.name}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(seller.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("📍 Adresse: ${seller.address}, ${seller.city}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SecondaryGreen)

            Spacer(modifier = Modifier.height(16.dp))

            // Non accessible button wrapper (MANDATORY prompt to connect!)
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryOrange.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (currentLang == "Français") "Connectez-vous pour ajouter des articles de boutique au panier." else "Log in to add store items to your cart.",
                        fontSize = 13.sp,
                        color = PrimaryOrange,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onAuthRedirect,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(if (currentLang == "Français") "Se connecter pour acheter" else "Login to buy products", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (currentLang == "Français") "📦 Produits disponibles" else "📦 Available Products",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            seller.products.forEach { prod ->
                FoodItemCard(
                    name = prod.name,
                    description = prod.description,
                    price = prod.price,
                    imageUrl = prod.imageUrl,
                    subTitle = null,
                    onAddToCart = { onAuthRedirect() },
                    onClick = { onProductClick(prod.id) },
                    currentLang = currentLang
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProductDetail(
    productId: String,
    sellerId: String,
    currentLang: String,
    onBack: () -> Unit,
    onAuthRedirect: () -> Unit
) {
    val sellers by KamerRepository.sellers.collectAsState()
    val seller = sellers.find { it.id == sellerId } ?: return
    val product = seller.products.find { it.id == productId } ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("← Back", color = SecondaryGreen, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Vendeur: ${seller.name}", fontSize = 12.sp, color = SecondaryGreen, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))

                Text(product.description, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (currentLang == "Français") "PRIX UNITAIRE" else "UNIT PRICE", fontSize = 10.sp, color = Color.Gray)
                        Text("${product.price.toInt()} XAF", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = SecondaryGreen)
                    }
                    Box(
                        modifier = Modifier
                            .background(SecondaryGreen.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (product.isAvailable) "Disponible" else "Rupture de stock",
                            color = SecondaryGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onAuthRedirect,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (currentLang == "Français") "Connectez-vous pour acheter" else "Login to buy this product",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BecomePartner(currentLang: String) {
    var selectedPartnerType by remember { mutableStateOf(PartnerType.RESTAURANT) }

    var businessName by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rccm by remember { mutableStateOf("") }
    var niu by remember { mutableStateOf("") }
    var extraDetails by remember { mutableStateOf("") } // vehicle for driver, etc.

    var showSuccessMessage by remember { mutableStateOf(false) }
    var registeredId by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = if (currentLang == "Français") "🤝 Rejoindre le réseau KamerDeliver" else "🤝 Become a Partner with KamerDeliver",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SecondaryGreen
        )
        Text(
            text = if (currentLang == "Français") "Remplissez le formulaire ci-dessous. Votre demande sera examinée par notre administration sous 24h." else "Fill out the application form. Admin will review and approve within 24 hours.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (showSuccessMessage) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SecondaryGreen.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SecondaryGreen, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "Français") "Demande Soumise avec Succès !" else "Application Submitted Successfully!",
                        fontWeight = FontWeight.Bold,
                        color = SecondaryGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (currentLang == "Français") "Numéro de dossier à conserver : $registeredId" else "Your Application ID is: $registeredId",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "Français") "Astuce: Allez sur l'onglet 'Suivi Demande' ou basculez sur le 'Dashboard Admin' pour approuver votre demande en direct !" else "Tip: Use 'Track Status' or switch to 'Admin' tab to approve this request instantly!",
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            showSuccessMessage = false
                            businessName = ""
                            managerName = ""
                            phone = ""
                            email = ""
                            rccm = ""
                            niu = ""
                            extraDetails = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen)
                    ) {
                        Text(if (currentLang == "Français") "Soumettre une autre demande" else "Submit Another")
                    }
                }
            }
        } else {
            // Type Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                PartnerType.values().forEach { type ->
                    val isSelected = selectedPartnerType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) SecondaryGreen else Color.Transparent)
                            .clickable { selectedPartnerType = type }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (type) {
                                PartnerType.RESTAURANT -> "Restaurant"
                                PartnerType.SELLER -> if (currentLang == "Français") "Vendeur" else "Seller"
                                PartnerType.DRIVER -> if (currentLang == "Français") "Livreur" else "Rider"
                            },
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dynamic Forms fields
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text(if (selectedPartnerType == PartnerType.DRIVER) (if (currentLang == "Français") "Nom complet" else "Full Name") else (if (currentLang == "Français") "Nom de l'entreprise / Restaurant" else "Business Name")) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                modifier = Modifier.fillMaxWidth().testTag("partner_business_name")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = managerName,
                onValueChange = { managerName = it },
                label = { Text(if (currentLang == "Français") "Nom du responsable" else "Manager Name") },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(if (currentLang == "Français") "Téléphone de contact" else "Contact Phone") },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                modifier = Modifier.fillMaxWidth().testTag("partner_phone")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                modifier = Modifier.fillMaxWidth()
            )

            // Special fields per partner type
            if (selectedPartnerType == PartnerType.RESTAURANT || selectedPartnerType == PartnerType.SELLER) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = rccm,
                    onValueChange = { rccm = it },
                    label = { Text(if (currentLang == "Français") "Numéro RCCM (Registre de commerce)" else "RCCM Registration Number") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = niu,
                    onValueChange = { niu = it },
                    label = { Text(if (currentLang == "Français") "Numéro NIU (Identifiant Unique)" else "NIU Tax Number") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (selectedPartnerType == PartnerType.DRIVER) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = extraDetails,
                    onValueChange = { extraDetails = it },
                    label = { Text(if (currentLang == "Français") "Type de véhicule (Moto, Vélo, Voiture) & Matricule" else "Vehicle details & license plate") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated File attachments indicators
            Text(
                text = if (currentLang == "Français") "📎 Pièces jointes requises (Simulées)" else "📎 Required Documents (Simulated)",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = if (selectedPartnerType == PartnerType.DRIVER) "• CNI recto/verso, Permis de conduire, Assurance" else "• Statuts de l'entreprise, Plan de localisation, Photo enseigne",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (businessName.isNotEmpty() && phone.isNotEmpty()) {
                        val genId = "REQ-" + (20..99).random()
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val newReq = PartnerRequest(
                            id = genId,
                            type = selectedPartnerType,
                            businessName = businessName,
                            managerName = managerName,
                            phone = phone,
                            email = email,
                            rccm = rccm,
                            niu = niu,
                            extraDetails = extraDetails,
                            status = RequestStatus.PENDING,
                            submissionDate = sdf.format(Date())
                        )
                        KamerRepository.registerPartner(newReq)
                        registeredId = genId
                        showSuccessMessage = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_partner_request")
            ) {
                Text(
                    text = if (currentLang == "Français") "Soumettre ma candidature" else "Submit Application",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun VerifyRequest(currentLang: String) {
    var requestIdInput by remember { mutableStateOf("") }
    val partnerRequests by KamerRepository.partnerRequests.collectAsState()
    var searchResult by remember { mutableStateOf<PartnerRequest?>(null) }
    var hasSearched by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (currentLang == "Français") "🔍 Vérifier l'état d'une demande" else "🔍 Track Application Status",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SecondaryGreen
        )
        Text(
            text = if (currentLang == "Français") "Saisissez votre numéro de dossier reçu à l'inscription (ex: REQ-44)" else "Enter your application ID (e.g. REQ-44)",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = requestIdInput,
                onValueChange = { requestIdInput = it },
                label = { Text(if (currentLang == "Français") "Numéro de demande" else "Application Number") },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SecondaryGreen, focusedLabelColor = SecondaryGreen),
                modifier = Modifier
                    .weight(1f)
                    .testTag("verify_request_input")
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    searchResult = partnerRequests.find { it.id.uppercase() == requestIdInput.trim().uppercase() }
                    hasSearched = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                modifier = Modifier.height(56.dp).testTag("verify_request_button")
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (hasSearched) {
            val result = searchResult
            if (result != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = result.businessName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            val statusColor = when (result.status) {
                                RequestStatus.PENDING -> PrimaryOrange
                                RequestStatus.IN_PROGRESS -> AccentYellow
                                RequestStatus.APPROVED -> SecondaryGreen
                                RequestStatus.REJECTED -> CameroonRed
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(statusColor)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = when (result.status) {
                                        RequestStatus.PENDING -> if (currentLang == "Français") "En attente" else "Pending"
                                        RequestStatus.IN_PROGRESS -> if (currentLang == "Français") "En cours" else "In Progress"
                                        RequestStatus.APPROVED -> if (currentLang == "Français") "Approuvée" else "Approved"
                                        RequestStatus.REJECTED -> if (currentLang == "Français") "Rejetée" else "Rejected"
                                    },
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Catégorie: ${result.type.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Responsable: ${result.managerName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Soumis le: ${result.submissionDate}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        if (result.status == RequestStatus.APPROVED) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SecondaryGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SecondaryGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "Français") "Félicitations ! Vous pouvez vous connecter à votre espace partenaire." else "Congratulations! You can now log into your partner dashboard.",
                                    fontSize = 12.sp,
                                    color = SecondaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = if (currentLang == "Français") "❌ Aucun dossier trouvé avec ce numéro." else "❌ No application found with this ID.",
                    color = CameroonRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun InfoSection(currentLang: String) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "ℹ️ Informations & Assistance",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SecondaryGreen
        )
        Spacer(modifier = Modifier.height(12.dp))

        // WhatsApp and Phone Action
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (currentLang == "Français") "Besoin d'aide immédiate ?" else "Need Immediate Assistance?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("WhatsApp Chat")
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Appeler")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (currentLang == "Français") "❔ Foire Aux Questions (FAQ)" else "❔ FAQ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        FAQItem(
            question = if (currentLang == "Français") "Comment commander ?" else "How to order?",
            answer = if (currentLang == "Français") "Parcourez les restaurants, connectez-vous, ajoutez au panier, puis payez avec MTN MoMo, Orange Money ou Espèces à la livraison." else "Browse restaurants, log in, add to cart, then pay via MTN MoMo, Orange Money, or Cash on Delivery."
        )
        FAQItem(
            question = if (currentLang == "Français") "Dans quelles villes livrez-vous ?" else "Which cities do you deliver to?",
            answer = "Nous livrons à Yaoundé, Douala, Bafoussam, Garoua, Bamenda, Kribi, Limbe et Bertoua."
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (currentLang == "Français") "📄 Conditions Générales & Politiques" else "📄 Legal Policies",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "• Conditions d'utilisation : Toutes les commandes passées sur KamerDeliver sont soumises à la disponibilité des stocks locaux.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "• Politique de remboursement : Remboursement intégral sur votre compte Mobile Money si la commande n'est pas acceptée ou livrée.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "• Politique de confidentialité : Vos données de géolocalisation et numéros de téléphone sont protégés et ne servent qu'au bon acheminement des livreurs.",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                Text(if (isExpanded) "▲" else "▼", fontSize = 10.sp, color = Color.Gray)
            }
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(answer, fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}
