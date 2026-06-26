package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MenuItem
import com.example.data.Restaurant
import com.example.ui.theme.*

@Composable
fun FoodItemCard(
    name: String,
    description: String,
    price: Double,
    imageUrl: String,
    subTitle: String?,
    onAddToCart: () -> Unit,
    onClick: () -> Unit,
    currentLang: String = "Français"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Image Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(UberGrayLight)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Details
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = UberBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subTitle != null) {
                Text(
                    text = subTitle,
                    fontSize = 11.sp,
                    color = UberGrayDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${price.toInt()} XAF",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = UberBlack
                )
                
                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .size(28.dp)
                        .background(UberBlack, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = UberWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
