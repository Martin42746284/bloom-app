package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.model.Discovery
import java.text.SimpleDateFormat
import java.util.*

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
            TopAppBar(
                title = { Text("Plant Journal", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = Color(0xFF4CAF50)) {
                Text("+", color = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(discoveries) { discovery ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clickable { onCardClick(discovery) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(discovery.imagePath),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp, 90.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(discovery.plantName, fontWeight = FontWeight.Bold)
                        Text(
                            "Ajouté le : " + SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(discovery.timestamp),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { onDeleteClick(discovery) }) {
                                Text("Supprimer", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
