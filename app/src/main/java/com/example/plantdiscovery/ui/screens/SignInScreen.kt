package com.example.plantdiscovery.ui.screens

import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import com.example.plantdiscovery.ui.theme.PrimaryGreen

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        onSignInClick = { email, password -> /* mock */ },
        onGoogleClick = {},
        onGoToSignUp = {}
    )
}

@Composable
fun SignInScreen(
    onSignInClick: (String, String) -> Unit,
    onGoogleClick: () -> Unit,
    onGoToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier.size(72.dp).background(Color(0xFFE9F7EF), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) { Text("🌱", fontSize = 36.sp) }
        Spacer(Modifier.height(36.dp))

        Text("Sign In", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onSignInClick(email, password) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(PrimaryGreen)
        ) { Text("Sign In", color = Color.White) }
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) { Text("Continue with Google") }

        Spacer(Modifier.height(18.dp))
        TextButton(onClick = onGoToSignUp) {
            Text("Don't have an account? Sign Up")
        }
    }
}
