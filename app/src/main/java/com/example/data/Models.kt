package com.example.data

import java.util.UUID

enum class UserRole {
    VISITOR,
    CLIENT
}

enum class SellerType {
    GROCERY,
    BAKERY,
    BUTCHERY,
    MARKET,
    LOCAL_PRODUCER
}

data class MenuItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val price: Double, // in XAF (Franc CFA)
    val isAvailable: Boolean = true,
    val imageUrl: String = "",
    val category: String,
    val stock: Int = 10,
    val variants: List<String> = emptyList() // e.g. ["Petite", "Moyenne", "Grande"]
)

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val price: Double, // in XAF
    val isAvailable: Boolean = true,
    val imageUrl: String = "",
    val category: String,
    val weight: String = "1kg",
    val stock: Int = 20,
    val sku: String = ""
)

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val author: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val reply: String? = null
)

data class Restaurant(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val logo: String,
    val banner: String,
    val address: String,
    val city: String,
    val rating: Float = 4.5f,
    val menu: List<MenuItem> = emptyList(),
    val hours: String = "08:00 - 22:00",
    val reviews: List<Review> = emptyList(),
    val isSponsored: Boolean = false,
    val categories: List<String> = emptyList()
)

data class Seller(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val logo: String,
    val banner: String,
    val address: String,
    val city: String,
    val type: SellerType,
    val rating: Float = 4.3f,
    val products: List<Product> = emptyList(),
    val reviews: List<Review> = emptyList()
)

enum class OrderStatus {
    PENDING,      // En attente d'acceptation par le resto/vendeur
    PREPARING,    // En cours de préparation / emballage
    OUT_FOR_DELIVERY, // En cours de livraison (livreur en route)
    DELIVERED,     // Commande livrée
    CANCELLED      // Commande annulée
}

data class CartItem(
    val menuItem: MenuItem? = null,
    val product: Product? = null,
    val quantity: Int = 1,
    val instructions: String = "",
    val selectedVariant: String? = null
) {
    val name: String get() = menuItem?.name ?: product?.name ?: "Article"
    val price: Double get() = menuItem?.price ?: product?.price ?: 0.0
    val total: Double get() = price * quantity
}

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String
)

data class Order(
    val id: String = UUID.randomUUID().toString().take(6).uppercase(),
    val items: List<CartItem>,
    val status: OrderStatus = OrderStatus.PENDING,
    val date: String,
    val clientName: String,
    val clientPhone: String,
    val deliveryAddress: String,
    val deliveryFee: Double = 1000.0, // XAF
    val totalAmount: Double, // total items + fee
    val paymentMethod: String, // MTN MoMo, Orange Money, Carte Bancaire, Paiement à la livraison
    val isPaid: Boolean = false,
    val restaurantId: String? = null,
    val restaurantName: String? = null,
    val sellerId: String? = null,
    val sellerName: String? = null,
    val driverId: String? = null,
    val driverName: String? = null,
    val driverPhone: String? = "677123456",
    val driverLat: Float = 4.05f, // Yaoundé/Douala coordinates approx
    val driverLng: Float = 9.70f,
    val chatMessages: List<ChatMessage> = emptyList()
)

enum class PartnerType {
    RESTAURANT,
    SELLER,
    DRIVER
}

enum class RequestStatus {
    PENDING,      // En attente
    IN_PROGRESS,  // En cours d'examen
    APPROVED,     // Approuvée
    REJECTED      // Rejetée
}

data class PartnerRequest(
    val id: String = UUID.randomUUID().toString().take(5).uppercase(),
    val type: PartnerType,
    val businessName: String,
    val managerName: String,
    val phone: String,
    val email: String,
    val rccm: String = "",
    val niu: String = "",
    val extraDetails: String = "", // e.g. vehicle type for driver
    val status: RequestStatus = RequestStatus.PENDING,
    val submissionDate: String
)

data class AddressBook(
    val title: String, // Maison, Bureau, etc.
    val address: String,
    val details: String = ""
)

data class UserProfile(
    val name: String = "Ivan Bayiga",
    val email: String = "ivan.bayiga@gmail.com",
    val phone: String = "690123456",
    val points: Int = 120, // Fidelity points
    val cashback: Double = 1500.0, // in XAF
    val language: String = "Français",
    val addresses: List<AddressBook> = listOf(
        AddressBook("Maison", "Nlongkak, Yaoundé", "Près de la boulangerie du peuple"),
        AddressBook("Bureau", "Akwa, Douala", "Immeuble Kamer, 3ème étage")
    )
)
