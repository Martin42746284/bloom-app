package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    onSignInClick: (email: String, password: String) -> Unit,
    onSignUpClick: (email: String, password: String) -> Unit,
    onGoogleClick: () -> Unit,
    isSignIn: Boolean,
    onSwitchMode: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Text("🔒", modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = if (isSignIn) 0 else 1) {
                Tab(text = { Text("Sign In") }, selected = isSignIn, onClick = { if (!isSignIn) onSwitchMode() })
                Tab(text = { Text("Sign Up") }, selected = !isSignIn, onClick = { if (isSignIn) onSwitchMode() })
            }
        }
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (isSignIn) onSignInClick(email, password)
                else onSignUpClick(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSignIn) "Sign In" else "Sign Up")
        }

        Spacer(Modifier.height(16.dp))
        Text("OR", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue with Google")
        }
    }
}
