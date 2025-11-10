package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.model.Discovery
import java.text.SimpleDateFormat
import java.util.*

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
                title = { Text("Discovery Details") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Retour") }
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
                        .height(180.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))
            }
            Text(discovery.plantName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(discovery.funFact, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Text(
                "Découvert le : " + SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(discovery.timestamp),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Supprimer l'entrée")
            }
        }
    }
}
