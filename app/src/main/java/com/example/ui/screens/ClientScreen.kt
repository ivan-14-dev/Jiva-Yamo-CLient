package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Uber Eats style palette
val UberBlack = Color(0xFF000000)
val UberWhite = Color(0xFFFFFFFF)
val UberGreen = Color(0xFF06C167) // Classic Uber Eats Green
val UberGrayLight = Color(0xFFF6F6F6) // Sleek light gray background/surface
val UberGrayMedium = Color(0xFFE8E8E8) // Visual border lines
val UberGrayDark = Color(0xFF545454) // Subdued body text
val UberRed = Color(0xFFE01919)
val UberYellow = Color(0xFFFFC000)

@Composable
fun ClientScreen(
    onNavigateBackToAuth: () -> Unit
) {
    var selectedClientTab by remember { mutableStateOf("dashboard") } // dashboard, search, favorites, orders, profile, cart

    val currentLang by KamerRepository.currentLanguage.collectAsState()
    val cart by KamerRepository.cart.collectAsState()
    val orders by KamerRepository.orders.collectAsState()

    val activeOrdersCount = orders.count { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED }

    Scaffold(
        containerColor = UberWhite,
        floatingActionButton = {
            if (cart.isNotEmpty() && selectedClientTab != "cart") {
                ExtendedFloatingActionButton(
                    onClick = { selectedClientTab = "cart" },
                    containerColor = UberBlack,
                    contentColor = UberWhite,
                    modifier = Modifier
                        .height(54.dp)
                        .testTag("floating_cart_button"),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Box {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(20.dp))
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(14.dp)
                                    .background(UberGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cart.sumOf { it.quantity }.toString(),
                                    color = UberWhite,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = if (currentLang == "Français") "Voir le panier" else "View Basket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = UberWhite,
                tonalElevation = 8.dp,
                modifier = Modifier.border(width = 1.dp, color = UberGrayMedium, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = selectedClientTab == "dashboard",
                    onClick = { selectedClientTab = "dashboard" },
                    icon = {
                        Icon(
                            imageVector = if (selectedClientTab == "dashboard") Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text(if (currentLang == "Français") "Accueil" else "Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UberBlack,
                        selectedTextColor = UberBlack,
                        unselectedIconColor = UberGrayDark,
                        unselectedTextColor = UberGrayDark,
                        indicatorColor = UberGrayLight
                    )
                )
                NavigationBarItem(
                    selected = selectedClientTab == "search",
                    onClick = { selectedClientTab = "search" },
                    icon = {
                        Icon(
                            imageVector = if (selectedClientTab == "search") Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Explore"
                        )
                    },
                    label = { Text(if (currentLang == "Français") "Découvrir" else "Browse", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UberBlack,
                        selectedTextColor = UberBlack,
                        unselectedIconColor = UberGrayDark,
                        unselectedTextColor = UberGrayDark,
                        indicatorColor = UberGrayLight
                    )
                )
                NavigationBarItem(
                    selected = selectedClientTab == "favorites",
                    onClick = { selectedClientTab = "favorites" },
                    icon = {
                        Icon(
                            imageVector = if (selectedClientTab == "favorites") Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorites"
                        )
                    },
                    label = { Text(if (currentLang == "Français") "Favoris" else "Baskets", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UberBlack,
                        selectedTextColor = UberBlack,
                        unselectedIconColor = UberGrayDark,
                        unselectedTextColor = UberGrayDark,
                        indicatorColor = UberGrayLight
                    )
                )
                NavigationBarItem(
                    selected = selectedClientTab == "orders",
                    onClick = { selectedClientTab = "orders" },
                    icon = {
                        Box {
                            Icon(
                                imageVector = if (selectedClientTab == "orders") Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                                contentDescription = "Orders"
                            )
                            if (activeOrdersCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(15.dp)
                                        .background(UberGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(activeOrdersCount.toString(), color = UberWhite, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    },
                    label = { Text(if (currentLang == "Français") "Suivi" else "Orders", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UberBlack,
                        selectedTextColor = UberBlack,
                        unselectedIconColor = UberGrayDark,
                        unselectedTextColor = UberGrayDark,
                        indicatorColor = UberGrayLight
                    )
                )
                NavigationBarItem(
                    selected = selectedClientTab == "profile",
                    onClick = { selectedClientTab = "profile" },
                    icon = {
                        Icon(
                            imageVector = if (selectedClientTab == "profile") Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text(if (currentLang == "Français") "Profil" else "Account", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UberBlack,
                        selectedTextColor = UberBlack,
                        unselectedIconColor = UberGrayDark,
                        unselectedTextColor = UberGrayDark,
                        indicatorColor = UberGrayLight
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(UberWhite)
        ) {
            when (selectedClientTab) {
                "dashboard" -> ClientDashboardScreen(
                    currentLang = currentLang,
                    onNavigateToTab = { selectedClientTab = it }
                )
                "search" -> ClientSearchScreen(
                    currentLang = currentLang,
                    onNavigateToTab = { selectedClientTab = it }
                )
                "favorites" -> ClientFavoritesScreen(
                    currentLang = currentLang,
                    onNavigateToTab = { selectedClientTab = it }
                )
                "cart" -> ClientCartScreen(
                    currentLang = currentLang,
                    onOrderPlaced = { selectedClientTab = "orders" }
                )
                "orders" -> ClientOrdersScreen(currentLang = currentLang)
                "profile" -> ClientProfileScreen(
                    currentLang = currentLang,
                    onLogout = {
                        KamerRepository.switchRole(UserRole.VISITOR)
                        onNavigateBackToAuth()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDashboardScreen(
    currentLang: String,
    onNavigateToTab: (String) -> Unit
) {
    val profile by KamerRepository.userProfile.collectAsState()
    val restaurants by KamerRepository.restaurants.collectAsState()
    val favoriteIds by KamerRepository.favoriteRestaurantIds.collectAsState()

    var isDeliverySelected by remember { mutableStateOf(true) }
    var selectedCategoryFilter by remember { mutableStateOf("Tout") }
    var showAddressSelector by remember { mutableStateOf(false) }
    var activeAddress by remember { mutableStateOf(profile.addresses.firstOrNull()?.address ?: "Nlongkak, Yaoundé") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(UberWhite)
    ) {
        // --- 1. Top Uber Header (Location picker and Toggle) ---
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Address Selector Row
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showAddressSelector = true }
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = UberBlack, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = if (currentLang == "Français") "Livrer maintenant" else "Deliver now",
                        fontSize = 11.sp,
                        color = UberGrayDark,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = activeAddress,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = UberBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 180.dp)
                        )
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = UberBlack, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Quick toggle for Delivery vs Pickup
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(UberGrayLight)
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isDeliverySelected) UberBlack else Color.Transparent)
                        .clickable { isDeliverySelected = true }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (currentLang == "Français") "Livraison" else "Delivery",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDeliverySelected) UberWhite else UberBlack
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (!isDeliverySelected) UberBlack else Color.Transparent)
                        .clickable { isDeliverySelected = false }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (currentLang == "Français") "À emporter" else "Pickup",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isDeliverySelected) UberWhite else UberBlack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. Uber Eats Clean Search Input Bar ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(UberGrayLight)
                .clickable { onNavigateToTab("search") }
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = null, tint = UberBlack, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (currentLang == "Français") "Plats, épicerie, boissons, etc." else "Food, groceries, drinks...",
                    color = UberGrayDark,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. Hub Visual Grid (Restaurants & Groceries) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Restaurants Big Tile
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable {
                        selectedCategoryFilter = "Tout"
                        onNavigateToTab("search")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UberGrayLight)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Load our beautiful generated image
                    Image(
                        painter = painterResource(id = R.drawable.img_uber_hero_1782408867274),
                        contentDescription = "Restaurants",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                )
                            )
                    )
                    Text(
                        text = if (currentLang == "Français") "Restaurants" else "Restaurants",
                        fontWeight = FontWeight.Bold,
                        color = UberWhite,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }
            }

            // Grocery Big Tile
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable {
                        onNavigateToTab("search")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UberGrayLight)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Load our beautiful generated image
                    Image(
                        painter = painterResource(id = R.drawable.img_grocery_hero_1782408914566),
                        contentDescription = "Groceries",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                )
                            )
                    )
                    Text(
                        text = if (currentLang == "Français") "Épiceries" else "Groceries",
                        fontWeight = FontWeight.Bold,
                        color = UberWhite,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. Sub-Categories Horizontal List ---
        Text(
            text = if (currentLang == "Français") "Explorez par catégorie" else "Explore Categories",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = UberBlack,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val subCategories = listOf(
                "Tout" to "🍽",
                "Cuisine Camerounaise" to "🍗",
                "Fast Food" to "🍔",
                "Pizza" to "🍕",
                "Grillades" to "🥩",
                "Chinois" to "🍜",
                "Dessert" to "🍰"
            )
            items(subCategories) { (catName, emoji) ->
                val isSelected = selectedCategoryFilter == catName
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedCategoryFilter = catName }
                        .width(72.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) UberGreen.copy(alpha = 0.15f) else UberGrayLight)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) UberGreen else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = catName,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) UberGreen else UberBlack,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. Clean Promotional Hero Card (Uber Eats Style Banner) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = UberBlack),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (currentLang == "Français") "OFFRE LIMITÉE" else "LIMITED OFFER",
                        color = UberGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (currentLang == "Français") "-20% de réduction immédiate" else "Get 20% off your entire basket",
                        color = UberWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (currentLang == "Français") "Code : MBOA20" else "Promo code: MBOA20",
                        color = UberWhite,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(UberGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎉", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 6. Beautiful Restaurant Feed ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedCategoryFilter == "Tout") {
                    if (currentLang == "Français") "Tous les restaurants" else "All Restaurants"
                } else {
                    selectedCategoryFilter
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = UberBlack
            )
            Text(
                text = if (currentLang == "Français") "Voir tout" else "See all",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = UberGreen,
                modifier = Modifier.clickable { onNavigateToTab("search") }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val filteredRestaurants = restaurants.filter { rest ->
            selectedCategoryFilter == "Tout" || rest.categories.contains(selectedCategoryFilter)
        }

        if (filteredRestaurants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (currentLang == "Français") "Aucun établissement disponible dans cette catégorie." else "No spots found for this category.",
                    color = UberGrayDark,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            filteredRestaurants.forEach { rest ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { onNavigateToTab("search") }
                ) {
                    // Wide 16:9 Image Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(UberGrayLight)
                    ) {
                        // cover banner
                        AsyncImage(
                            model = rest.banner,
                            contentDescription = rest.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Sponsored tag overlay
                        if (rest.isSponsored) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(UberWhite, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                    .align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = if (currentLang == "Français") "Sponsorisé" else "Sponsored",
                                    color = UberBlack,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        // Heart overlay icon in top right
                        val isFav = favoriteIds.contains(rest.id)
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(36.dp)
                                .background(UberWhite, CircleShape)
                                .clickable { KamerRepository.toggleFavoriteRestaurant(rest.id) }
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) UberRed else UberBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Details row below cover photo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rest.logo, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = rest.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = UberBlack
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${rest.description}",
                                fontSize = 12.sp,
                                color = UberGrayDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "1000 XAF " + (if (currentLang == "Français") "Frais de livraison" else "Delivery Fee"),
                                    fontSize = 11.sp,
                                    color = UberGrayDark,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("•", fontSize = 11.sp, color = UberGrayDark)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "25-35 min",
                                    fontSize = 11.sp,
                                    color = UberGrayDark,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Rating pill on the right
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(UberGrayLight)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = rest.rating.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = UberBlack
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(Icons.Default.Star, contentDescription = null, tint = UberYellow, modifier = Modifier.size(13.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Bottom Address Selector Modal Sheet Dialog
    if (showAddressSelector) {
        AlertDialog(
            onDismissRequest = { showAddressSelector = false },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAddressSelector = false }) {
                    Text("Fermer", color = UberGreen)
                }
            },
            title = {
                Text(
                    text = if (currentLang == "Français") "Sélectionner une adresse" else "Select Address",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = UberBlack
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    profile.addresses.forEach { addr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (activeAddress == addr.address) UberGreen.copy(alpha = 0.08f) else Color.Transparent)
                                .clickable {
                                    activeAddress = addr.address
                                    showAddressSelector = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = if (activeAddress == addr.address) UberGreen else UberGrayDark)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(addr.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UberBlack)
                                Text(addr.address, fontSize = 12.sp, color = UberGrayDark)
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = UberWhite
        )
    }
}

@Composable
fun ClientSearchScreen(
    currentLang: String,
    onNavigateToTab: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val restaurants by KamerRepository.restaurants.collectAsState()
    val sellers by KamerRepository.sellers.collectAsState()

    var activeFilterType by remember { mutableStateOf("All") } // All, Restaurant, Seller

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UberWhite)
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (currentLang == "Français") "Recherche (ex: Ndolé, Burger...)" else "Search (e.g. Ndole, Bread...)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = UberBlack) },
            shape = RoundedCornerShape(26.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = UberBlack,
                unfocusedBorderColor = UberGrayMedium,
                focusedContainerColor = UberGrayLight,
                unfocusedContainerColor = UberGrayLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("client_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Pills Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "All" to (if (currentLang == "Français") "Tout" else "All"),
                "Restaurant" to "Restaurants",
                "Seller" to (if (currentLang == "Français") "Magasins" else "Shops")
            ).forEach { (key, label) ->
                val isSelected = activeFilterType == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) UberBlack else UberGrayLight)
                        .clickable { activeFilterType = key }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) UberWhite else UberBlack,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Results Section
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // Restaurants Menu Items (Food Items)
            if (activeFilterType == "All" || activeFilterType == "Restaurant") {
                Text(
                    text = if (currentLang == "Français") "🍽 Menus Restaurants" else "🍽 Restaurant Menus",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = UberBlack,
                    modifier = Modifier.padding(bottom = 12.dp, top = 6.dp)
                )

                val matchedRestaurants = restaurants.filter { r ->
                    searchQuery.isEmpty() || r.name.contains(searchQuery, ignoreCase = true) || r.menu.any { m -> m.name.contains(searchQuery, ignoreCase = true) }
                }

                val menuItemsWithRest = matchedRestaurants.flatMap { r ->
                    r.menu.filter { m -> searchQuery.isEmpty() || m.name.contains(searchQuery, ignoreCase = true) }
                        .map { m -> Pair(r, m) }
                }

                if (menuItemsWithRest.isEmpty()) {
                    Text(
                        text = if (currentLang == "Français") "Aucun repas trouvé." else "No food items found.",
                        color = UberGrayDark,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    menuItemsWithRest.chunked(2).forEach { pairList ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pairList.forEach { (rest, item) ->
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                                    border = BorderStroke(1.dp, UberGrayMedium)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = item.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = UberBlack,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "De: ${rest.name}",
                                            fontSize = 10.sp,
                                            color = UberGrayDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${item.price.toInt()} XAF",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = UberGreen
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                KamerRepository.addToCart(CartItem(menuItem = item, quantity = 1))
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = UberBlack),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .testTag("add_item_${item.id}")
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Add, contentDescription = null, tint = UberWhite, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(if (currentLang == "Français") "Ajouter" else "Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            if (pairList.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Grocery Products
            if (activeFilterType == "All" || activeFilterType == "Seller") {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (currentLang == "Français") "🥦 Produits Magasins & Épiceries" else "🥦 Store Products",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = UberBlack,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val matchedSellers = sellers.filter { s ->
                    searchQuery.isEmpty() || s.name.contains(searchQuery, ignoreCase = true) || s.products.any { p -> p.name.contains(searchQuery, ignoreCase = true) }
                }

                val storeItemsWithSeller = matchedSellers.flatMap { s ->
                    s.products.filter { p -> searchQuery.isEmpty() || p.name.contains(searchQuery, ignoreCase = true) }
                        .map { p -> Pair(s, p) }
                }

                if (storeItemsWithSeller.isEmpty()) {
                    Text(
                        text = if (currentLang == "Français") "Aucun article d'épicerie trouvé." else "No grocery items found.",
                        color = UberGrayDark,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    storeItemsWithSeller.chunked(2).forEach { pairList ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pairList.forEach { (seller, prod) ->
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                                    border = BorderStroke(1.dp, UberGrayMedium)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = prod.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = UberBlack,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "De: ${seller.name}",
                                            fontSize = 10.sp,
                                            color = UberGrayDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${prod.price.toInt()} XAF",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = UberGreen
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = {
                                                KamerRepository.addToCart(CartItem(product = prod, quantity = 1))
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = UberBlack),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(30.dp)
                                                .testTag("add_product_${prod.id}")
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Add, contentDescription = null, tint = UberWhite, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(if (currentLang == "Français") "Ajouter" else "Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            if (pairList.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientCartScreen(
    currentLang: String,
    onOrderPlaced: () -> Unit
) {
    val cart by KamerRepository.cart.collectAsState()

    var couponCode by remember { mutableStateOf("") }
    var couponApplied by remember { mutableStateOf(false) }
    var couponError by remember { mutableStateOf(false) }
    var instructions by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("MTN MoMo") }

    val itemsTotal = cart.sumOf { it.total }
    val discount = if (couponApplied) itemsTotal * 0.20 else 0.0
    val deliveryFee = 1000.0 // Fixed Cameroon standard shipping
    val finalTotal = itemsTotal - discount + deliveryFee

    val scrollState = rememberScrollState()

    if (cart.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🛒", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (currentLang == "Français") "Votre panier est vide" else "Your basket is empty",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UberBlack
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (currentLang == "Français") "Parcourez des repas ou articles d'épicerie pour passer commande." else "Add items from restaurants and shops to get started.",
                fontSize = 14.sp,
                color = UberGrayDark,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(UberWhite)
                .padding(16.dp)
        ) {
            Text(
                text = if (currentLang == "Français") "Mon Panier" else "My Basket",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = UberBlack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cart Items
            cart.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                    border = BorderStroke(1.dp, UberGrayMedium)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = UberBlack)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${item.price.toInt()} XAF", fontSize = 12.sp, color = UberGrayDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Total: ${item.total.toInt()} XAF", fontWeight = FontWeight.Bold, color = UberGreen, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { KamerRepository.updateCartQuantity(item, item.quantity - 1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = UberBlack)
                            }
                            Text(
                                text = item.quantity.toString(),
                                fontWeight = FontWeight.Bold,
                                color = UberBlack,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = { KamerRepository.updateCartQuantity(item, item.quantity + 1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = UberBlack)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { KamerRepository.removeFromCart(item) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = UberRed)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions text field
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text(if (currentLang == "Français") "Instructions de livraison (ex: Sonner à la barrière)" else "Delivery notes (e.g. ring bell)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UberBlack,
                    unfocusedBorderColor = UberGrayMedium,
                    focusedLabelColor = UberBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Coupon Code
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = couponCode,
                    onValueChange = {
                        couponCode = it
                        couponError = false
                    },
                    label = { Text(if (currentLang == "Français") "Code Promo" else "Promo Code") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UberBlack,
                        unfocusedBorderColor = UberGrayMedium,
                        focusedLabelColor = UberBlack
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("coupon_input")
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (couponCode.uppercase() == "MBOA20") {
                            couponApplied = true
                            couponError = false
                        } else {
                            couponError = true
                            couponApplied = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UberBlack),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .testTag("coupon_apply_button")
                ) {
                    Text(if (currentLang == "Français") "Appliquer" else "Apply", fontWeight = FontWeight.Bold)
                }
            }
            if (couponApplied) {
                Text(
                    text = if (currentLang == "Français") "✓ Coupon MBOA20 activé (-20% !)" else "✓ Coupon MBOA20 activated (-20%!)",
                    color = UberGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else if (couponError) {
                Text(
                    text = if (currentLang == "Français") "✗ Code invalide. Essayez le code MBOA20" else "✗ Invalid code. Try MBOA20",
                    color = UberRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Methods Selection
            Text(
                text = if (currentLang == "Français") "Mode de paiement" else "Payment Method",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = UberBlack
            )
            Spacer(modifier = Modifier.height(10.dp))
            listOf("MTN MoMo", "Orange Money", "Carte Bancaire", "Paiement à la livraison").forEach { pay ->
                val isSelected = selectedPaymentMethod == pay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) UberGreen.copy(alpha = 0.08f) else Color.Transparent)
                        .clickable { selectedPaymentMethod = pay }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pay,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) UberGreen else UberBlack,
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = UberGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cost Summary breakdown
            Card(
                colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                border = BorderStroke(1.dp, UberGrayMedium),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (currentLang == "Français") "Sous-total articles" else "Subtotal", color = UberBlack)
                        Text("${itemsTotal.toInt()} XAF", color = UberBlack, fontWeight = FontWeight.Medium)
                    }
                    if (couponApplied) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(if (currentLang == "Français") "Réduction de 20%" else "Promo Discount", color = UberRed)
                            Text("-${discount.toInt()} XAF", color = UberRed, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (currentLang == "Français") "Frais de livraison" else "Delivery Fee", color = UberBlack)
                        Text("${deliveryFee.toInt()} XAF", color = UberBlack, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "Français") "Livraison estimée : 25-35 mins" else "Estimated delivery: 25-35 mins",
                        color = UberGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = UberGrayMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (currentLang == "Français") "TOTAL DU PANIER" else "TOTAL", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = UberBlack)
                        Text("${finalTotal.toInt()} XAF", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = UberBlack)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order Trigger Button
            Button(
                onClick = {
                    KamerRepository.placeOrder(selectedPaymentMethod, if (couponApplied) "MBOA20" else "")
                    onOrderPlaced()
                },
                colors = ButtonDefaults.buttonColors(containerColor = UberGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("checkout_order_button")
            ) {
                Text(
                    text = if (currentLang == "Français") "Commander maintenant" else "Place Order",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = UberWhite
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ClientOrdersScreen(
    currentLang: String
) {
    val orders by KamerRepository.orders.collectAsState()
    var selectedOrderForTracking by remember { mutableStateOf<Order?>(null) }
    var chatMessageInput by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    if (selectedOrderForTracking != null) {
        val trackingOrder = orders.find { it.id == selectedOrderForTracking!!.id } ?: selectedOrderForTracking!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(UberWhite)
                .padding(16.dp)
        ) {
            TextButton(
                onClick = { selectedOrderForTracking = null },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = UberBlack, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == "Français") "Retour aux commandes" else "Back to Orders",
                        color = UberBlack,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = (if (currentLang == "Français") "Suivi commande #" else "Tracking Order #") + trackingOrder.id,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UberBlack
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (currentLang == "Français") "Statut : " else "Status: ", color = UberGrayDark, fontSize = 13.sp)
                Text(
                    text = trackingOrder.status.name,
                    fontWeight = FontWeight.Bold,
                    color = UberGreen,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real-time map rendering using beautiful Jetpack Compose Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                border = BorderStroke(1.dp, UberGrayMedium),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        // Draw virtual elegant roads
                        drawLine(color = UberGrayMedium, start = Offset(80f, 60f), end = Offset(canvasWidth - 80f, 60f), strokeWidth = 14f)
                        drawLine(color = UberGrayMedium, start = Offset(canvasWidth - 120f, 60f), end = Offset(canvasWidth - 120f, canvasHeight - 60f), strokeWidth = 14f)
                        drawLine(color = UberGrayMedium, start = Offset(80f, canvasHeight - 120f), end = Offset(canvasWidth - 80f, canvasHeight - 120f), strokeWidth = 14f)
                        drawLine(color = UberGrayMedium, start = Offset(120f, 60f), end = Offset(120f, canvasHeight - 60f), strokeWidth = 14f)

                        // Highlight delivery route
                        drawLine(color = UberGreen.copy(alpha = 0.4f), start = Offset(120f, 60f), end = Offset(canvasWidth - 120f, 60f), strokeWidth = 8f)
                        drawLine(color = UberGreen.copy(alpha = 0.4f), start = Offset(canvasWidth - 120f, 60f), end = Offset(canvasWidth - 120f, canvasHeight - 120f), strokeWidth = 8f)

                        // Draw Restaurant/Shop Origin Point
                        drawCircle(color = UberGreen, radius = 18f, center = Offset(120f, 60f))

                        // Draw Client Destination Point
                        drawCircle(color = UberBlack, radius = 18f, center = Offset(canvasWidth - 120f, canvasHeight - 120f))
                    }

                    // Floating labels overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .background(UberGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = trackingOrder.restaurantName ?: trackingOrder.sellerName ?: "Vendeur",
                            color = UberWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .background(UberBlack, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (currentLang == "Français") "Maison (Vous)" else "Home (You)",
                            color = UberWhite,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Live moving driver indicator based on coordinates simulated in KamerRepository
                    if (trackingOrder.status == OrderStatus.OUT_FOR_DELIVERY) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(UberBlack, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🛵", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (currentLang == "Français") "Livreur en route" else "Rider is near",
                                    color = UberWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(UberGrayLight, RoundedCornerShape(20.dp))
                                .border(1.dp, UberGrayMedium, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = when (trackingOrder.status) {
                                    OrderStatus.PENDING -> if (currentLang == "Français") "Attente de validation..." else "Awaiting store..."
                                    OrderStatus.PREPARING -> if (currentLang == "Français") "Préparation en cuisine..." else "Preparing your food..."
                                    OrderStatus.DELIVERED -> if (currentLang == "Français") "Commande livrée !" else "Arrived!"
                                    else -> "Chargement..."
                                },
                                color = UberBlack,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Order progression step list
            Card(
                colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                border = BorderStroke(1.dp, UberGrayMedium),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (currentLang == "Français") "Étapes de votre commande" else "Order Status Timeline",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = UberBlack
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val steps = listOf(
                        OrderStatus.PENDING to (if (currentLang == "Français") "Demande envoyée" else "Request received"),
                        OrderStatus.PREPARING to (if (currentLang == "Français") "Préparation en cours" else "Preparing your items"),
                        OrderStatus.OUT_FOR_DELIVERY to (if (currentLang == "Français") "Livreur en route (Moto)" else "Out for delivery"),
                        OrderStatus.DELIVERED to (if (currentLang == "Français") "Livreur arrivé ! Bon appétit" else "Delivered!")
                    )

                    steps.forEachIndexed { index, (status, label) ->
                        val isDone = trackingOrder.status.ordinal >= status.ordinal
                        val isActive = trackingOrder.status == status
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(if (isDone) UberGreen else UberGrayMedium, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = UberWhite, modifier = Modifier.size(14.dp))
                                } else {
                                    Text((index + 1).toString(), color = UberGrayDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = label,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (isActive) UberGreen else if (isDone) UberBlack else UberGrayDark,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chat messenger section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UberWhite),
                border = BorderStroke(1.dp, UberGrayMedium)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(UberGreen.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = UberGreen, modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = if (currentLang == "Français") "Message avec le coursier" else "Chat with Courier",
                            fontWeight = FontWeight.Bold,
                            color = UberBlack,
                            fontSize = 15.sp
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = UberGrayMedium)

                    // Messages list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (trackingOrder.chatMessages.isEmpty()) {
                            Text(
                                text = if (currentLang == "Français") "Aucun message. Le chat s'activera automatiquement." else "No messages. Chat opens when courier starts.",
                                fontSize = 12.sp,
                                color = UberGrayDark,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        } else {
                            trackingOrder.chatMessages.forEach { msg ->
                                val isMe = msg.sender == "Client" || msg.sender == "Vous"
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isMe) UberGreen else UberGrayLight)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = msg.message,
                                            fontSize = 13.sp,
                                            color = if (isMe) UberWhite else UberBlack
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${msg.sender} • ${msg.timestamp}",
                                        fontSize = 10.sp,
                                        color = UberGrayDark,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick-reply chat text field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = chatMessageInput,
                            onValueChange = { chatMessageInput = it },
                            placeholder = { Text(if (currentLang == "Français") "Écrire au coursier..." else "Write to courier...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_text"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UberBlack,
                                unfocusedBorderColor = UberGrayMedium,
                                focusedLabelColor = UberBlack
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (chatMessageInput.isNotEmpty()) {
                                    KamerRepository.addChatMessage(trackingOrder.id, chatMessageInput, "Vous")
                                    chatMessageInput = ""
                                }
                            },
                            modifier = Modifier
                                .testTag("chat_send_button")
                                .background(UberBlack, CircleShape)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = UberWhite)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dynamic Action button
            if (trackingOrder.status == OrderStatus.PENDING || trackingOrder.status == OrderStatus.PREPARING) {
                Button(
                    onClick = {
                        KamerRepository.updateOrderStatus(trackingOrder.id, OrderStatus.CANCELLED)
                        selectedOrderForTracking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UberRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(if (currentLang == "Français") "Annuler la commande" else "Cancel Order", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = {
                        // Action placeholder
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UberBlack),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(if (currentLang == "Français") "Faire une réclamation" else "Need Help?", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    } else {
        // Active orders listing
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UberWhite)
                .padding(16.dp)
        ) {
            Text(
                text = if (currentLang == "Français") "Vos Commandes" else "Your Orders",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = UberBlack
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentLang == "Français") "Aucune commande passée." else "No active or past orders.",
                        color = UberGrayDark,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(orders) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOrderForTracking = order }
                                .testTag("order_card_${order.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                            border = BorderStroke(1.dp, UberGrayMedium)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "ID: #${order.id}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = UberBlack
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when (order.status) {
                                                    OrderStatus.PENDING -> UberBlack
                                                    OrderStatus.PREPARING -> UberYellow
                                                    OrderStatus.OUT_FOR_DELIVERY -> UberGreen
                                                    OrderStatus.DELIVERED -> Color.Gray
                                                    OrderStatus.CANCELLED -> UberRed
                                                }
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(order.status.name, color = UberWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = order.restaurantName ?: order.sellerName ?: "Yamo Delivery",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = UberBlack
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${order.items.size} " + (if (currentLang == "Français") "articles" else "items") + "  •  ${order.totalAmount.toInt()} XAF",
                                    fontSize = 12.sp,
                                    color = UberGrayDark
                                )
                                Text(
                                    text = if (currentLang == "Français") "Paiement : ${order.paymentMethod}" else "Paid via: ${order.paymentMethod}",
                                    fontSize = 11.sp,
                                    color = UberGrayDark
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (currentLang == "Français") "➜ Suivre en temps réel" else "➜ Track in real time",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = UberGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientProfileScreen(
    currentLang: String,
    onLogout: () -> Unit
) {
    val profile by KamerRepository.userProfile.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(UberWhite)
            .padding(16.dp)
    ) {
        // Profile Summary Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(UberGrayLight, CircleShape)
                    .border(1.dp, UberGrayMedium, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🧑", fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(profile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = UberBlack)
                Text(profile.email, fontSize = 12.sp, color = UberGrayDark)
                Text(profile.phone, fontSize = 12.sp, color = UberGrayDark)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fidelity Points card (Uber Clean Minimal style)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = UberBlack),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentLang == "Français") "Programme Fidélité Yamo" else "Yamo Loyalty Rewards",
                        color = UberWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = UberWhite)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${profile.points} " + (if (currentLang == "Français") "Points accumulés" else "Points earned"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = UberGreen
                )
                Text(
                    text = if (currentLang == "Français") "Cashback convertible : ${profile.cashback.toInt()} XAF" else "Cashback available: ${profile.cashback.toInt()} XAF",
                    color = UberGrayMedium,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Address list
        Text(
            text = if (currentLang == "Français") "Adresses enregistrées" else "Saved Addresses",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = UberBlack
        )
        Spacer(modifier = Modifier.height(10.dp))

        profile.addresses.forEach { addr ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                border = BorderStroke(1.dp, UberGrayMedium)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = UberBlack)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(addr.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UberBlack)
                        Text(addr.address, fontSize = 12.sp, color = UberGrayDark)
                        if (addr.details.isNotEmpty()) {
                            Text(addr.details, fontSize = 11.sp, color = UberGrayDark)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Options
        Text(
            text = if (currentLang == "Français") "Préférences" else "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = UberBlack
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = UberWhite),
            border = BorderStroke(1.dp, UberGrayMedium)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            KamerRepository.setLanguage(if (currentLang == "Français") "English" else "Français")
                        }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (currentLang == "Français") "Langue de l'application" else "App Language", color = UberBlack, fontSize = 14.sp)
                    Text(currentLang, fontWeight = FontWeight.Bold, color = UberGreen, fontSize = 14.sp)
                }

                HorizontalDivider(color = UberGrayMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (currentLang == "Français") "Notifications WhatsApp / SMS" else "WhatsApp Notifications", color = UberBlack, fontSize = 14.sp)
                    Text(if (currentLang == "Français") "Activé" else "Enabled", color = UberGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Sign out button
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = UberRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button")
        ) {
            Text(
                text = if (currentLang == "Français") "Se déconnecter" else "Sign Out",
                fontWeight = FontWeight.Bold,
                color = UberWhite
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ClientFavoritesScreen(
    currentLang: String,
    onNavigateToTab: (String) -> Unit
) {
    val favoriteIds by KamerRepository.favoriteRestaurantIds.collectAsState()
    val restaurants by KamerRepository.restaurants.collectAsState()
    val favoriteRestaurants = restaurants.filter { favoriteIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UberWhite)
            .padding(16.dp)
    ) {
        Text(
            text = if (currentLang == "Français") "Vos Favoris" else "Your Baskets",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = UberBlack
        )
        Text(
            text = if (currentLang == "Français") "Établissements enregistrés" else "Restaurants and grocery shops you starred",
            fontSize = 12.sp,
            color = UberGrayDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favoriteRestaurants.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("❤️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (currentLang == "Français") "Aucun favori pour le moment" else "No saved spots yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = UberBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (currentLang == "Français") "Appuyez sur le cœur d'un restaurant pour l'ajouter ici." else "Tap the heart on a restaurant card to save it.",
                        fontSize = 13.sp,
                        color = UberGrayDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteRestaurants) { rest ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTab("search") },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UberGrayLight),
                        border = BorderStroke(1.dp, UberGrayMedium)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(rest.logo, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(rest.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = UberBlack)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(rest.description, fontSize = 12.sp, color = UberGrayDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("🕒 ${rest.hours}  •  ${rest.city}", fontSize = 11.sp, color = UberGreen, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { KamerRepository.toggleFavoriteRestaurant(rest.id) }) {
                                Icon(Icons.Default.Favorite, contentDescription = "Remove", tint = UberRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
