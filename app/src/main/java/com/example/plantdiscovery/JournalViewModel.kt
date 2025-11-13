package com.example.plantdiscovery

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantdiscovery.model.Discovery
import com.example.plantdiscovery.repository.DiscoveryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat  // ✅ Import nécessaire
import java.util.*                 // ✅ Import nécessaire pour Date et Locale

class JournalViewModel(private val repository: DiscoveryRepository) : ViewModel() {

    val discoveries: StateFlow<List<Discovery>> = repository.getAllDiscoveries()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addDiscovery(plantName: String, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val savedPath = saveImageToInternalStorage(context, imageUri)

            val discovery = Discovery(
                plantName = plantName,
                imagePath = savedPath,
                timestamp = System.currentTimeMillis()
            )
            repository.insertDiscovery(discovery)
        }
    }

    fun deleteDiscovery(discovery: Discovery) {
        viewModelScope.launch {
            // Supprimer le fichier image
            deleteImageFile(discovery.imagePath)
            // Supprimer l'entrée de la base de données
            repository.deleteDiscovery(discovery)
        }
    }

    private suspend fun saveImageToInternalStorage(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            try {
                // Créer un nom de fichier unique
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "plant_$timeStamp.jpg"

                // Créer le répertoire si nécessaire
                val imagesDir = File(context.filesDir, "images")
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }

                // Créer le fichier de destination
                val file = File(imagesDir, fileName)

                // Copier l'image depuis l'URI vers le stockage interne
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IOException("Unable to open input stream from URI: $uri")

                // Vérifier que le fichier a bien été créé
                if (!file.exists() || file.length() == 0L) {
                    throw IOException("Failed to save image: file is empty or does not exist")
                }

                // Retourner le chemin absolu
                file.absolutePath

            } catch (e: Exception) {
                // Logger l'erreur
                android.util.Log.e("JournalViewModel", "Error saving image to internal storage", e)
                throw IOException("Failed to save image: ${e.message}", e)
            }
        }
    }

    private fun deleteImageFile(imagePath: String) {
        try {
            val file = File(imagePath)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            android.util.Log.e("JournalViewModel", "Error deleting image file", e)
        }
    }
}
