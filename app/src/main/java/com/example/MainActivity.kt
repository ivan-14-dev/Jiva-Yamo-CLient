package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KamerRepository
import com.example.data.UserRole
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ClientScreen
import com.example.ui.screens.VisitorScreen
import com.example.ui.theme.AccentYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SecondaryGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by KamerRepository.isDarkMode.collectAsState()
            MyApplicationTheme(darkTheme = isDarkMode) {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    val currentRole by KamerRepository.currentUserRole.collectAsState()
    val currentLang by KamerRepository.currentLanguage.collectAsState()

    var showAuthFlow by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Column {
                // High-fidelity edge-to-edge friendly custom top bar
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 44.dp) // accommodate status bar insets
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "KamerDeliver",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = SecondaryGreen
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "★",
                                        color = AccentYellow,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = if (currentLang == "Français") "Cameroun Delivery Platform" else "Cameroon Delivery Platform",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }

                            // Theme controls container
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Elegant Theme Toggle Capsule (Light / Dark)
                                val isDarkMode by KamerRepository.isDarkMode.collectAsState()
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF2F2F2))
                                        .clickable { KamerRepository.toggleDarkMode() }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("theme_toggle_btn"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isDarkMode) "🌙 Sombre" else "☀️ Clair",
                                        color = if (isDarkMode) Color.White else Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showAuthFlow && currentRole == UserRole.VISITOR) {
                AuthScreen(
                    onAuthSuccess = { showAuthFlow = false },
                    onSkipAuth = { showAuthFlow = false }
                )
            } else {
                when (currentRole) {
                    UserRole.VISITOR -> VisitorScreen(
                        onNavigateToAuth = { showAuthFlow = true }
                    )
                    UserRole.CLIENT -> ClientScreen(
                        onNavigateBackToAuth = { showAuthFlow = true }
                    )
                    else -> VisitorScreen(
                        onNavigateToAuth = { showAuthFlow = true }
                    )
                }
            }
        }
    }
}
