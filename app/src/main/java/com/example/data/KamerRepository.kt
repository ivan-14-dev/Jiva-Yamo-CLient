package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object KamerRepository {
    private val scope = CoroutineScope(Dispatchers.Default)

    // Language
    private val _currentLanguage = MutableStateFlow("Français")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    // Dark mode state
    private val _isDarkMode = MutableStateFlow(false) // default light mode
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    // Role state - initialized to VISITOR for free browsing!
    private val _currentUserRole = MutableStateFlow(UserRole.VISITOR)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    fun switchRole(role: UserRole) {
        _currentUserRole.value = role
    }

    // Cities
    val cities = listOf("Yaoundé", "Douala", "Bafoussam", "Garoua", "Bamenda", "Kribi", "Limbe", "Bertoua")

    // Categories
    val foodCategories = listOf("Tout", "Cuisine Camerounaise", "Fast Food", "Pizza", "Grillades", "Chinois", "Dessert")
    val sellerCategories = listOf("Tout", "Épiceries", "Boulangeries", "Boucheries", "Marchés", "Producteurs locaux")

    // Reviews helper
    private fun getMockReviews() = listOf(
        Review(author = "Marie NGO", rating = 5f, comment = "Excellent Ndolé! Très bien assaisonné et chaud à la livraison.", date = "2026-06-20", reply = "Merci Marie! Nous cuisinons avec amour."),
        Review(author = "Jean-Pierre", rating = 4f, comment = "Le poulet DG était très bon, mais un peu d'attente pour la livraison.", date = "2026-06-18"),
        Review(author = "Amadou", rating = 5f, comment = "Les portions sont généreuses. Je recommande vivement!", date = "2026-06-15", reply = "Ravi que cela vous plaise, Amadou!")
    )

    // Pre-populated Restaurants
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    // Pre-populated Sellers
    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers.asStateFlow()

    // Favorites
    private val _favoriteRestaurantIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteRestaurantIds: StateFlow<Set<String>> = _favoriteRestaurantIds.asStateFlow()

    // Cart
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    // Orders
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Partner Requests (Approved, Pending, etc.)
    private val _partnerRequests = MutableStateFlow<List<PartnerRequest>>(emptyList())
    val partnerRequests: StateFlow<List<PartnerRequest>> = _partnerRequests.asStateFlow()

    // Profile
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        // Populate static mock data
        initializeMockData()
    }

    private fun initializeMockData() {
        // Predefined restaurants with local Cameroonian foods and prices in FCFA (XAF)
        val restList = listOf(
            Restaurant(
                id = "rest_tanty_mel",
                name = "Chez Tanty Mélanie",
                description = "Spécialités camerounaises authentiques de Douala. Ndolé aux crevettes, Poulet DG, Achu, Kondre et sauces locales cuisinées au feu de bois.",
                logo = "🍳",
                banner = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600",
                address = "Bonapriso, Rue des Palmiers",
                city = "Douala",
                rating = 4.8f,
                isSponsored = true,
                categories = listOf("Cuisine Camerounaise", "Grillades"),
                menu = listOf(
                    MenuItem("tm_ndole", "Ndolé Royal aux Crevettes", "Plat traditionnel composé de feuilles de ndolé, d'arachides fraîches cuites, de crevettes sautées et de plantains mûrs frits.", 3500.0, true, "", "Cuisine Camerounaise", 15, listOf("Moyenne Portion", "Grande Portion")),
                    MenuItem("tm_pouletdg", "Poulet DG Classique", "Mijoté de poulet fermier aux plantains frits, carottes, haricots verts et poivrons colorés dans une sauce onctueuse.", 4500.0, true, "", "Cuisine Camerounaise", 12),
                    MenuItem("tm_achu", "Achu complet du Nord-Ouest", "Taro pilé servi avec sa sauce jaune onctueuse, de la peau de bœuf croustillante (kanda) et de la viande de bœuf.", 4000.0, true, "", "Cuisine Camerounaise", 8),
                    MenuItem("tm_poisson", "Bar de Kribi braisé au piment", "Poisson bar frais braisé avec des épices locales africaines, servi avec des frites de plantain mûr (miondo) et piment.", 5000.0, true, "", "Grillades", 10),
                    MenuItem("tm_beignets", "Beignets-Haricots-Bouillie (BHB)", "Le petit-déjeuner national camerounais: beignets soufflés, haricots sautés épicés et bouillie de maïs chaud.", 1500.0, true, "", "Cuisine Camerounaise", 30)
                ),
                reviews = getMockReviews()
            ),
            Restaurant(
                id = "rest_burger_star",
                name = "Burger Star Yaoundé",
                description = "Le roi des burgers gourmets à Yaoundé. Viande fraîche hachée locale, pain artisanal brioché et frites maison.",
                logo = "🍔",
                banner = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600",
                address = "Bastos, face ambassade",
                city = "Yaoundé",
                rating = 4.5f,
                categories = listOf("Fast Food"),
                menu = listOf(
                    MenuItem("bs_chiz", "Star Cheeseburger", "Pain brioché, bœuf 150g, double cheddar fondu, oignons caramélisés, sauce maison et frites.", 3000.0, true, "", "Fast Food", 20, listOf("Standard", "Double Steak (+1500 XAF)")),
                    MenuItem("bs_chicken", "Crispy Mboa Chicken", "Filet de poulet pané croustillant au piment doux du Cameroun, salade fraîche, mayonnaise épicée.", 3500.0, true, "", "Fast Food", 14),
                    MenuItem("bs_pizza", "Pizza Mboa Reggae", "Pizza croustillante garnie de morceaux de bœuf épicé (suya), piment doux, oignons frais et mozzarella filante.", 5000.0, true, "", "Pizza", 18, listOf("Moyenne", "Géante (+2500 XAF)"))
                ),
                reviews = listOf(
                    Review(author = "Arthur F.", rating = 4f, comment = "Le meilleur burger de Yaoundé ! Les frites sont croustillantes.", date = "2026-06-22")
                )
            ),
            Restaurant(
                id = "rest_chez_wou",
                name = "Le Palais Chinois Chez Wou",
                description = "La référence de la haute gastronomie asiatique à Yaoundé et Douala. Canard laqué, nems croustillants et riz cantonais.",
                logo = "🍜",
                banner = "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=600",
                address = "Ancien Bastos",
                city = "Yaoundé",
                rating = 4.6f,
                categories = listOf("Chinois", "Tout"),
                menu = listOf(
                    MenuItem("cw_nems", "Nems au Poulet (4 pièces)", "Nems frits ultra croustillants farcis au poulet et légumes, servis avec sauce nuoc-mâm.", 2000.0, true, "", "Chinois", 25),
                    MenuItem("cw_riz", "Riz cantonais spécial Chez Wou", "Riz sauté aux crevettes, jambon local, œufs brouillés et petits pois frais.", 3500.0, true, "", "Chinois", 30),
                    MenuItem("cw_canard", "Canard laqué de Pékin", "Canard fondant à la peau croustillante caramélisée aux cinq épices, servi avec de fines crêpes.", 8500.0, true, "", "Chinois", 5)
                ),
                reviews = listOf(
                    Review(author = "Divine K.", rating = 5f, comment = "Service impeccable, plats savoureux, parfait pour les dîners professionnels.", date = "2026-06-21")
                )
            ),
            Restaurant(
                id = "rest_kribi_beach",
                name = "Kribi Beach Grill",
                description = "Poisson fraîchement pêché braisé sur la plage de Kribi. Une expérience authentique les pieds dans le sable.",
                logo = "🐟",
                banner = "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=600",
                address = "Plage de Ngoye",
                city = "Kribi",
                rating = 4.9f,
                categories = listOf("Grillades", "Tout"),
                menu = listOf(
                    MenuItem("kb_bar", "Gros Bar de mer braisé", "Bar frais braisé au feu de bois avec miondo, plantain frit et piment noir local (nkui/condre épices).", 6000.0, true, "", "Grillades", 10),
                    MenuItem("kb_crevettes", "Crevettes géantes de l'Océan", "6 brochettes de crevettes tigrées grillées à l'ail des ours sauvage et citron vert de Kribi.", 7000.0, true, "", "Grillades", 8)
                ),
                reviews = listOf(
                    Review(author = "Loïc S.", rating = 5f, comment = "Un régal total ! Les pieds dans l'eau avec le bruit des vagues.", date = "2026-06-24")
                )
            )
        )
        _restaurants.value = restList

        // Predefined sellers with local products
        val sellerList = listOf(
            Seller(
                id = "sell_sandaga",
                name = "Super Marché Sandaga",
                description = "Le plus grand marché de vivres frais à Douala. Livraison directe de fruits de saison, légumes frais du Noun et tubercules.",
                logo = "🥑",
                banner = "https://images.unsplash.com/photo-1542838132-92c53300491e?w=600",
                address = "Marché Sandaga, Boulevard de la République",
                city = "Douala",
                type = SellerType.MARKET,
                rating = 4.6f,
                products = listOf(
                    Product("sd_avo", "Avocat beurre de Mbouda", "Gros avocat onctueux et très savoureux, idéal pour salades ou tartines.", 500.0, true, "", "Fruits", "Pièce", 50, "SKU-AVO-MBO"),
                    Product("sd_reg", "Régime de plantains mûrs", "Régime moyen de plantains jaunes sucrés, parfait pour faire du bon 'missole' ou du plantain pilé.", 3500.0, true, "", "Fruits", "Régime (~10kg)", 15, "SKU-REG-PLN"),
                    Product("sd_tom", "Panier de Tomates de Foumbot", "Tomates fraîches et fermes cultivées localement, parfaites pour vos sauces tomates quotidiennes.", 2000.0, true, "", "Légumes", "Panier (~5kg)", 20, "SKU-PAN-TOM"),
                    Product("sd_pim", "Piment fort rouge du Cameroun", "Piment extrêmement piquant sélectionné pour son parfum intense.", 200.0, true, "", "Légumes", "Sachet (100g)", 40, "SKU-SACH-PIM")
                ),
                reviews = listOf(
                    Review(author = "Alice M.", rating = 4f, comment = "Les avocats de Mbouda sont vraiment 'beurre'! Excellente fraîcheur.", date = "2026-06-23")
                )
            ),
            Seller(
                id = "sell_zepol",
                name = "Boulangerie Zépol",
                description = "La boulangerie historique de Yaoundé. Pain chaud toutes les heures, pâtisseries fines et sandwichs mboa.",
                logo = "🥖",
                banner = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=600",
                address = "Avenue Kennedy",
                city = "Yaoundé",
                type = SellerType.BAKERY,
                rating = 4.7f,
                products = listOf(
                    Product("zp_bag", "Baguette de Pain croustillante", "Baguette de pain blanc traditionnelle cuite au four à pierre, toujours livrée tiède.", 150.0, true, "", "Boulangerie", "Pièce (250g)", 200, "SKU-ZP-BAG"),
                    Product("zp_cro", "Croissant pur beurre d'Ancre", "Croissant français croustillant et feuilleté fait maison.", 500.0, true, "", "Boulangerie", "Pièce", 60, "SKU-ZP-CRO"),
                    Product("zp_gateau", "Gâteau aux fruits exotiques", "Génoise moelleuse garnie d'ananas de Penja, de mangue du Nord et de fruits de la passion.", 8000.0, true, "", "Boulangerie", "Gâteau (6-8 parts)", 5, "SKU-ZP-GAT")
                ),
                reviews = listOf(
                    Review(author = "Ferdinand", rating = 5f, comment = "Leur baguette est irréprochable. Livrée super chaude !", date = "2026-06-24")
                )
            ),
            Seller(
                id = "sell_bouch_centre",
                name = "Boucherie du Centre Yaoundé",
                description = "Viandes de bœuf de l'Adamaoua, porcs locaux et volailles nettoyées prêtes à cuire. Qualité supérieure garantie.",
                logo = "🥩",
                banner = "https://images.unsplash.com/photo-1603048588665-791ca8aea617?w=600",
                address = "Marché Central",
                city = "Yaoundé",
                type = SellerType.BUTCHERY,
                rating = 4.5f,
                products = listOf(
                    Product("bc_boeuf", "Filet de Bœuf Adamaoua", "Morceau de bœuf tendre et dégraissé, idéal pour brochettes ou rôtis.", 4500.0, true, "", "Viande", "Kg", 30, "SKU-BC-BOE"),
                    Product("bc_poulet", "Poulet de chair nettoyé", "Poulet entier élevé localement, plume enlevée et vidé, prêt à mariner.", 3500.0, true, "", "Viande", "Pièce (~1.5kg)", 45, "SKU-BC-PUL")
                )
            )
        )
        _sellers.value = sellerList

        // Initial empty state for active orders, except one sample past delivered order for history
        _orders.value = listOf(
            Order(
                id = "KD-4183",
                items = listOf(
                    CartItem(
                        menuItem = restList[0].menu[0], // Ndolé
                        quantity = 2,
                        selectedVariant = "Grande Portion"
                    )
                ),
                status = OrderStatus.DELIVERED,
                date = "Hier, 19:30",
                clientName = "Ivan Bayiga",
                clientPhone = "690123456",
                deliveryAddress = "Nlongkak, Yaoundé",
                deliveryFee = 1000.0,
                totalAmount = 8000.0,
                paymentMethod = "MTN MoMo",
                isPaid = true,
                restaurantId = "rest_tanty_mel",
                restaurantName = "Chez Tanty Mélanie",
                driverId = "driver_amadou",
                driverName = "Amadou",
                driverPhone = "677112233"
            )
        )

        // Initial Partner Requests
        _partnerRequests.value = listOf(
            PartnerRequest(
                id = "REQ-19",
                type = PartnerType.RESTAURANT,
                businessName = "Le Marseillais Pizzas",
                managerName = "Jean-Claude",
                phone = "699112233",
                email = "jean.claude@marseillais.com",
                rccm = "RC-YDE-2026-B-102",
                niu = "M0626123456X",
                status = RequestStatus.APPROVED,
                submissionDate = "2026-06-10"
            ),
            PartnerRequest(
                id = "REQ-44",
                type = PartnerType.DRIVER,
                businessName = "Livreur Moto",
                managerName = "Moussa Sali",
                phone = "677334455",
                email = "moussa.sali@gmail.com",
                extraDetails = "Moto Yamaha Crux (Immatriculation LT-123-AB)",
                status = RequestStatus.PENDING,
                submissionDate = "2026-06-24"
            )
        )
    }

    // Toggle Favorite Restaurant
    fun toggleFavoriteRestaurant(id: String) {
        val current = _favoriteRestaurantIds.value
        if (current.contains(id)) {
            _favoriteRestaurantIds.value = current - id
        } else {
            _favoriteRestaurantIds.value = current + id
        }
    }

    // Cart operations
    fun addToCart(item: CartItem) {
        val current = _cart.value.toMutableList()
        // If item already exists (matching item id and selected variant), increase quantity
        val existingIndex = current.indexOfFirst {
            val sameMenu = it.menuItem?.id != null && it.menuItem.id == item.menuItem?.id
            val sameProd = it.product?.id != null && it.product.id == item.product?.id
            val sameVariant = it.selectedVariant == item.selectedVariant
            (sameMenu || sameProd) && sameVariant
        }
        if (existingIndex != -1) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + item.quantity)
        } else {
            current.add(item)
        }
        _cart.value = current
    }

    fun removeFromCart(item: CartItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    fun updateCartQuantity(item: CartItem, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(item)
            return
        }
        val current = _cart.value.toMutableList()
        val index = current.indexOf(item)
        if (index != -1) {
            current[index] = current[index].copy(quantity = quantity)
            _cart.value = current
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Place order
    fun placeOrder(paymentMethod: String, coupon: String = ""): Order {
        val cartItems = _cart.value
        if (cartItems.isEmpty()) throw IllegalStateException("Le panier est vide")

        val itemsTotal = cartItems.sumOf { it.total }
        val discount = if (coupon.uppercase() == "MBOA20") itemsTotal * 0.20 else 0.0
        val deliveryFee = 1000.0 // XAF fixed delivery
        val total = itemsTotal - discount + deliveryFee

        val firstItem = cartItems.first()
        val restId = firstItem.menuItem?.let {
            // Find which restaurant has this item
            _restaurants.value.find { r -> r.menu.any { m -> m.id == it.id } }?.id
        }
        val restName = firstItem.menuItem?.let {
            _restaurants.value.find { r -> r.menu.any { m -> m.id == it.id } }?.name
        }

        val sellId = firstItem.product?.let {
            // Find which seller has this product
            _sellers.value.find { s -> s.products.any { p -> p.id == it.id } }?.id
        }
        val sellName = firstItem.product?.let {
            _sellers.value.find { s -> s.products.any { p -> p.id == it.id } }?.name
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val newOrder = Order(
            items = cartItems,
            status = OrderStatus.PENDING,
            date = sdf.format(Date()),
            clientName = _userProfile.value.name,
            clientPhone = _userProfile.value.phone,
            deliveryAddress = _userProfile.value.addresses.firstOrNull()?.address ?: "Yaoundé, Cameroun",
            deliveryFee = deliveryFee,
            totalAmount = total,
            paymentMethod = paymentMethod,
            isPaid = paymentMethod != "Paiement à la livraison",
            restaurantId = restId,
            restaurantName = restName,
            sellerId = sellId,
            sellerName = sellName,
            driverId = null,
            driverName = null
        )

        val ordersList = _orders.value.toMutableList()
        ordersList.add(0, newOrder) // add at top
        _orders.value = ordersList

        // Clear cart
        clearCart()

        // Trigger order lifecycle simulation!
        simulateOrderLifecycle(newOrder.id)

        return newOrder
    }

    // Partner registration
    fun registerPartner(request: PartnerRequest) {
        val list = _partnerRequests.value.toMutableList()
        list.add(0, request)
        _partnerRequests.value = list
    }

    // Admin updates partner status
    fun updatePartnerStatus(requestId: String, status: RequestStatus) {
        val list = _partnerRequests.value.map {
            if (it.id == requestId) it.copy(status = status) else it
        }
        _partnerRequests.value = list
    }

    // Updates order status (e.g. from Resto Dashboard or Driver Dashboard)
    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        val list = _orders.value.map {
            if (it.id == orderId) {
                var orderCopy = it.copy(status = status)
                // Assign a virtual driver when order leaves restaurant
                if (status == OrderStatus.OUT_FOR_DELIVERY && it.driverId == null) {
                    orderCopy = orderCopy.copy(
                        driverId = "driver_moussa",
                        driverName = "Moussa Sali (Moto)",
                        driverPhone = "677334455"
                    )
                }
                orderCopy
            } else it
        }
        _orders.value = list
    }

    // Add chat message
    fun addChatMessage(orderId: String, message: String, sender: String) {
        val list = _orders.value.map {
            if (it.id == orderId) {
                val chat = it.chatMessages.toMutableList()
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                chat.add(ChatMessage(sender = sender, message = message, timestamp = sdf.format(Date())))
                it.copy(chatMessages = chat)
            } else it
        }
        _orders.value = list
    }

    // Simulate ordering lifecycle so the user gets real-time reactions on the screen!
    private fun simulateOrderLifecycle(orderId: String) {
        scope.launch {
            delay(4000) // 4 seconds before restaurant accepts
            var order = _orders.value.find { it.id == orderId } ?: return@launch
            if (order.status == OrderStatus.PENDING) {
                updateOrderStatus(orderId, OrderStatus.PREPARING)
                addChatMessage(orderId, "Bonjour, nous avons bien reçu votre commande ! Préparation en cours en cuisine. 🍳", "Restaurant")
            }

            delay(6000) // 6 seconds in preparation before dispatch
            order = _orders.value.find { it.id == orderId } ?: return@launch
            if (order.status == OrderStatus.PREPARING) {
                updateOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY)
                addChatMessage(orderId, "Commande prête ! Je la récupère à l'instant et je démarre ma moto. À tout de suite !", "Moussa Sali (Livreur)")
            }

            // Simulate GPS coordinates moving
            for (i in 1..4) {
                delay(3000)
                order = _orders.value.find { it.id == orderId } ?: return@launch
                if (order.status == OrderStatus.OUT_FOR_DELIVERY) {
                    val progress = i / 4.0f
                    // Change coordinates slightly to simulate movement between Douala and Yaoundé
                    val updatedList = _orders.value.map {
                        if (it.id == orderId) {
                            it.copy(
                                driverLat = 4.05f + (0.01f * progress),
                                driverLng = 9.70f + (0.015f * progress)
                            )
                        } else it
                    }
                    _orders.value = updatedList
                    if (i == 2) {
                        addChatMessage(orderId, "Je viens de passer le carrefour Bastos, j'arrive dans 5 minutes. Gardez votre téléphone à portée de main !", "Moussa Sali (Livreur)")
                    }
                }
            }

            delay(4000)
            order = _orders.value.find { it.id == orderId } ?: return@launch
            if (order.status == OrderStatus.OUT_FOR_DELIVERY) {
                updateOrderStatus(orderId, OrderStatus.DELIVERED)
                addChatMessage(orderId, "Je suis devant votre porte ! Bon appétit ! N'hésitez pas à me laisser une note sur KamerDeliver.", "Moussa Sali (Livreur)")
            }
        }
    }

    // Add custom restaurant menu items (used by restaurant dashboard!)
    fun addRestaurantMenuItem(restaurantId: String, item: MenuItem) {
        val list = _restaurants.value.map {
            if (it.id == restaurantId) {
                val updatedMenu = it.menu.toMutableList()
                updatedMenu.add(item)
                it.copy(menu = updatedMenu)
            } else it
        }
        _restaurants.value = list
    }

    fun removeRestaurantMenuItem(restaurantId: String, itemId: String) {
        val list = _restaurants.value.map {
            if (it.id == restaurantId) {
                val updatedMenu = it.menu.filter { item -> item.id != itemId }
                it.copy(menu = updatedMenu)
            } else it
        }
        _restaurants.value = list
    }

    // Add seller products
    fun addSellerProduct(sellerId: String, product: Product) {
        val list = _sellers.value.map {
            if (it.id == sellerId) {
                val updatedProducts = it.products.toMutableList()
                updatedProducts.add(product)
                it.copy(products = updatedProducts)
            } else it
        }
        _sellers.value = list
    }

    fun removeSellerProduct(sellerId: String, productId: String) {
        val list = _sellers.value.map {
            if (it.id == sellerId) {
                val updatedProducts = it.products.filter { p -> p.id != productId }
                it.copy(products = updatedProducts)
            } else it
        }
        _sellers.value = list
    }

    // Toggle Restaurant Menu Item Availability (Uber Eats stock manager style)
    fun toggleMenuItemAvailability(restaurantId: String, itemId: String) {
        val list = _restaurants.value.map {
            if (it.id == restaurantId) {
                val updatedMenu = it.menu.map { item ->
                    if (item.id == itemId) item.copy(isAvailable = !item.isAvailable) else item
                }
                it.copy(menu = updatedMenu)
            } else it
        }
        _restaurants.value = list
    }

    // Update Seller Product Stock (Uber Eats Grocery manager style)
    fun updateProductStock(sellerId: String, productId: String, newStock: Int) {
        val list = _sellers.value.map {
            if (it.id == sellerId) {
                val updatedProducts = it.products.map { p ->
                    if (p.id == productId) p.copy(stock = newStock.coerceAtLeast(0)) else p
                }
                it.copy(products = updatedProducts)
            } else it
        }
        _sellers.value = list
    }
}
