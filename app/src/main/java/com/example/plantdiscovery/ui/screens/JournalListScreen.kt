package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.ui.theme.PrimaryGreen
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun JournalListScreenPreview() {
    val sampleDiscoveries = listOf(
        Discovery(
            id = 1,
            userId = "user123",
            plantName = "Monstera Deliciosa",
            funFact = "Cette plante est originaire des forêts tropicales.",
            imagePath = "https://upload.wikimedia.org/wikipedia/commons/4/4d/Monstera_deliciosa.jpg",
            timestamp = System.currentTimeMillis()
        ),
        Discovery(
            id = 2,
            userId = "user456",
            plantName = "Ficus Lyrata",
            funFact = "Connue comme plante violon.",
            imagePath = "",
            timestamp = System.currentTimeMillis()
        )
    )

    JournalListScreen(
        discoveries = sampleDiscoveries,
        onAddClick = {},
        onCardClick = {},
        onDeleteClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    discoveries: List<Discovery>,
    onAddClick: () -> Unit,
    onCardClick: (Discovery) -> Unit,
    onDeleteClick: (Discovery) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🌱",
                        fontSize = 28.sp
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryGreen,
                modifier = Modifier.size(64.dp)
            ) {
                Text("+", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 90.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(discoveries) { discovery ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCardClick(discovery) },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(discovery.imagePath),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌿", fontSize = 18.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                discovery.plantName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📅", fontSize = 14.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(discovery.timestamp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
