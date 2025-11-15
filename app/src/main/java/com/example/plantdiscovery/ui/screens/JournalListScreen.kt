package com.example.plantdiscovery.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    discoveries: List<Discovery>,
    onAddClick: () -> Unit,
    onCardClick: (Discovery) -> Unit,
    onDeleteClick: (Discovery) -> Unit,
    onSignOut: () -> Unit = {}
) {
    // User info
    val currentUser = FirebaseAuth.getInstance().currentUser
    val displayName = currentUser?.displayName ?: "Plant Lover"
    val email = currentUser?.email ?: ""
    var showMenu by remember { mutableStateOf(false) }

    // Animation state
    val fabScale by animateFloatAsState(
        targetValue = if (discoveries.isEmpty()) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "My Garden",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${discoveries.size} plants discovered",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .widthIn(min = 200.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = email,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Divider()

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ExitToApp,
                                            contentDescription = "Sign Out",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            "Sign Out",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                onClick = {
                                    showMenu = false
                                    onSignOut()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = {
                    Text(
                        "Add Plant",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        AnimatedContent(
            targetState = discoveries.isEmpty(),
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            },
            label = "content_transition"
        ) { isEmpty ->
            if (isEmpty) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "No Plants Yet",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Start your garden by adding your first plant!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                // Plants list
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 16.dp,
                        bottom = padding.calculateBottomPadding() + 100.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(discoveries, key = { it.id }) { discovery ->
                        PlantCard(
                            discovery = discovery,
                            onClick = { onCardClick(discovery) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlantCard(
    discovery: Discovery,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Image with gradient overlay
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(140.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(discovery.imagePath),
                    contentDescription = discovery.plantName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = discovery.plantName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                if (discovery.funFact.isNotBlank()) {
                    Text(
                        text = discovery.funFact,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(discovery.timestamp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Chevron icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View details",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ========== PREVIEWS ==========

@Preview(name = "Light Mode - With Plants", showBackground = true)
@Composable
private fun JournalListScreenPreviewLight() {
    PlantdiscoveryTheme(darkTheme = false) {
        JournalListScreen(
            discoveries = listOf(
                Discovery(
                    id = 1,
                    plantName = "Monstera Deliciosa",
                    funFact = "Known for its unique split leaves that develop as it matures.",
                    imagePath = "",
                    timestamp = System.currentTimeMillis()
                ),
                Discovery(
                    id = 2,
                    plantName = "Snake Plant",
                    funFact = "One of the best air-purifying plants and very easy to care for.",
                    imagePath = "",
                    timestamp = System.currentTimeMillis() - 86400000
                ),
                Discovery(
                    id = 3,
                    plantName = "Peace Lily",
                    funFact = "Beautiful white flowers and excellent at removing toxins.",
                    imagePath = "",
                    timestamp = System.currentTimeMillis() - 172800000
                )
            ),
            onAddClick = {},
            onCardClick = {},
            onDeleteClick = {},
            onSignOut = {}
        )
    }
}

@Preview(name = "Dark Mode - With Plants", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun JournalListScreenPreviewDark() {
    PlantdiscoveryTheme(darkTheme = true) {
        JournalListScreen(
            discoveries = listOf(
                Discovery(
                    id = 1,
                    plantName = "Cactus",
                    funFact = "Can store water for months and thrive in extreme conditions.",
                    imagePath = "",
                    timestamp = System.currentTimeMillis()
                )
            ),
            onAddClick = {},
            onCardClick = {},
            onDeleteClick = {},
            onSignOut = {}
        )
    }
}

@Preview(name = "Empty State", showBackground = true)
@Composable
private fun JournalListScreenPreviewEmpty() {
    PlantdiscoveryTheme {
        JournalListScreen(
            discoveries = emptyList(),
            onAddClick = {},
            onCardClick = {},
            onDeleteClick = {},
            onSignOut = {}
        )
    }
}
