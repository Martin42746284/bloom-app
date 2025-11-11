package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.ui.theme.PrimaryGreen

@Preview(showBackground = true)
@Composable
fun CaptureScreenPreview() {
    CaptureScreen(
        imagePath = null,
        onCaptureClick = {},
        onGalleryClick = {},
        loading = false,
        onCancel = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imagePath != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Image Preview", color = Color(0xFFAAAAAA))
                }
            }

            Spacer(Modifier.height(32.dp))

            if (loading) {
                CircularProgressIndicator(color = PrimaryGreen)
            } else {
                Button(
                    onClick = onCaptureClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("📸")
                    Spacer(Modifier.width(8.dp))
                    Text("Capture Photo", color = Color.White)
                }
                Spacer(Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onGalleryClick,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("🖼️")
                    Spacer(Modifier.width(8.dp))
                    Text("Select from Gallery", color = Color.Black)
                }
            }
        }
    }
}
