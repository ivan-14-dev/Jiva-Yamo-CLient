package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Restaurant
import com.example.ui.theme.*

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    currentLang: String = "Français"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() }
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
                model = restaurant.banner,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Sponsored tag overlay
            if (restaurant.isSponsored) {
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
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(36.dp)
                    .background(UberWhite, CircleShape)
                    .clickable { onToggleFavorite() }
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) UberRed else UberBlack,
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
                    Text(restaurant.logo, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = restaurant.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = UberBlack
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = restaurant.description,
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
                        text = restaurant.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = UberBlack
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(Icons.Default.Star, contentDescription = null, tint = UberBlack, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}
