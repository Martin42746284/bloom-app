package com.example.plantdiscovery.model

data class Discovery(
    val id: Int,
    val userId: String,
    val plantName: String,
    val funFact: String,
    val imagePath: String,
    val timestamp: Long
)
