package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            "Bienvenue sur KamerDeliver",
            "Le meilleur du marché local camerounais livré directement chez vous.",
            "🚚"
        ),
        OnboardingPage(
            "Restaurants & Épicerie",
            "Commandez vos plats préférés ou faites vos courses en quelques clics.",
            "🥘"
        ),
        OnboardingPage(
            "Suivi en Temps Réel",
            "Suivez votre livreur sur la carte et chattez avec lui en direct.",
            "📍"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UberWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = pages[pageIndex]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(page.emoji, fontSize = 80.sp)
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = page.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = UberBlack
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = page.description,
                    fontSize = 16.sp,
                    color = UberGrayDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        // Indicator
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == index) UberGreen else UberGrayMedium)
                )
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage < 2) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    onFinished()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = UberBlack),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (pagerState.currentPage == 2) "Commencer" else "Suivant",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (pagerState.currentPage < 2) {
            TextButton(onClick = onFinished) {
                Text("Passer", color = UberGrayDark)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

data class OnboardingPage(val title: String, val description: String, val emoji: String)
