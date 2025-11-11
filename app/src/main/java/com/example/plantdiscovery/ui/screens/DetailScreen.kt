package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.ui.theme.ErrorRed
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        discovery = Discovery(
            id = 1,
            userId = "user123",
            plantName = "Monstera Deliciosa",
            funFact = "Cette plante est originaire des forêts tropicales d'Amérique centrale.",
            imagePath = "https://upload.wikimedia.org/wikipedia/commons/4/4d/Monstera_deliciosa.jpg",
            timestamp = System.currentTimeMillis()
        ),
        onBack = {},
        onDelete = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    discovery: Discovery,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discovery Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            if (discovery.imagePath.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(discovery.imagePath),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(18.dp))
                )
                Spacer(Modifier.height(18.dp))
            }

            Text(
                discovery.plantName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(6.dp))

            Text(
                "Discovered: " + SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm", Locale.getDefault()).format(discovery.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Spacer(Modifier.height(16.dp))

            Text(
                discovery.funFact,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Delete Entry", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
