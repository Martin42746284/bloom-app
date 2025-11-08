package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CaptureScreen(
    imagePath: String?,
    onCaptureClick: () -> Unit,
    onGalleryClick: () -> Unit,
    loading: Boolean,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Discovery") },
                navigationIcon = { IconButton(onClick = onCancel) { Icon(Icons.Default.PhotoCamera, null) } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp)
            ) {
                if (imagePath != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Image Preview")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            if (loading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = onCaptureClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Icon(Icons.Default.PhotoCamera, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Capture Photo")
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onGalleryClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Icon(Icons.Default.PhotoLibrary, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Select from Gallery")
                }
            }
        }
    }
}
