package com.example.plantdiscovery.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.plantdiscovery.ui.theme.PrimaryGreen
import com.example.plantdiscovery.ui.theme.PrimaryGreenLight
import com.example.plantdiscovery.ui.theme.TextSecondary
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CaptureScreen(
    imagePath: String?,
    onCaptureClick: (Uri) -> Unit,
    onGalleryClick: (Uri) -> Unit,
    loading: Boolean,
    onCancel: () -> Unit,
    onSave: (String, Uri) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var plantName by remember { mutableStateOf("") }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ ORDRE CORRECT: Déclarer cameraLauncher AVANT cameraPermissionLauncher

    // 1. Launcher pour capturer une photo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            selectedImageUri = capturedImageUri
            onCaptureClick(capturedImageUri!!)
        }
    }

    // 2. Launcher pour demander la permission caméra (utilise cameraLauncher)
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission accordée, lancer la caméra
            val uri = createImageUri(context)
            capturedImageUri = uri
            cameraLauncher.launch(uri)  // ✅ Maintenant cameraLauncher est défini
        } else {
            showPermissionDialog = true
        }
    }

    // 3. Launcher pour sélectionner depuis la galerie
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            onGalleryClick(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),  // ✅ Ajouté pour éviter clipping
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            "Add New Plant",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Image preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryGreenLight),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected plant image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    "📷\nNo image selected",
                    fontSize = 24.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Camera button
            OutlinedButton(
                onClick = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = !loading
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 24.sp)
                    Text("Camera", fontSize = 12.sp)
                }
            }

            // Gallery button
            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = !loading
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🖼️", fontSize = 24.sp)
                    Text("Gallery", fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Plant name input
        OutlinedTextField(
            value = plantName,
            onValueChange = { plantName = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        // Save button
        Button(
            onClick = {
                if (selectedImageUri != null && plantName.isNotBlank()) {
                    onSave(plantName, selectedImageUri!!)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            enabled = !loading && selectedImageUri != null && plantName.isNotBlank()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Save Plant", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Cancel button
        TextButton(
            onClick = onCancel,
            enabled = !loading
        ) {
            Text("Cancel")
        }
    }

    // Permission denied dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Camera Permission Required") },
            text = { Text("Please grant camera permission in app settings to take photos.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

// Fonction helper pour créer un URI de fichier temporaire
private fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "PLANT_$timeStamp.jpg"
    val storageDir = File(context.getExternalFilesDir(null), "Pictures")

    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }

    val imageFile = File(storageDir, imageFileName)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}
