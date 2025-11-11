package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantdiscovery.ui.theme.PrimaryGreen

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        onSignUpClick = { email, password -> /* mock */ },
        onGoogleClick = {},
        onGoToSignIn = {}
    )
}

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String) -> Unit,
    onGoogleClick: () -> Unit,
    onGoToSignIn: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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

        Text("Sign Up", style = MaterialTheme.typography.titleLarge)
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
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth()
        )
        if (showError && password != confirmPassword) {
            Text(
                "Passwords do not match!",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                showError = (password != confirmPassword)
                if (!showError) {
                    onSignUpClick(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(PrimaryGreen)
        ) { Text("Sign Up", color = Color.White) }
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) { Text("Continue with Google") }

        Spacer(Modifier.height(18.dp))
        TextButton(onClick = onGoToSignIn) {
            Text("Already have an account? Sign In")
        }
    }
}
