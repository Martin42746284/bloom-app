package com.example.plantdiscovery.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    SignUpScreen(
//        onSignUpClick = { email, password -> /* mock */ },
//        onGoogleClick = {},
//        onGoToSignIn = {}
//    )
//}

@Composable
fun SignUpScreen(
    loading: Boolean,
    error: String?,
    success: Boolean,
    onSignUpClick: (String, String, String) -> Unit,
    onGoToSignIn: () -> Unit,
    onErrorDismiss: () -> Unit = {},
    onSuccessDismiss: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = { onSignUpClick(email, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
            } else {
                Text("Sign Up", color = Color.White)
            }
        }

        if (error != null) {
            Spacer(Modifier.height(6.dp))
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { onErrorDismiss() }
            )
            Spacer(Modifier.height(4.dp))
        }

        if (success) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Account created! Please sign in.",
                color = PrimaryGreen,
                modifier = Modifier.clickable { onSuccessDismiss() }
            )
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = {/* onGoogleClick() */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = !loading
        ) { Text("Continue with Google") }

        Spacer(Modifier.height(18.dp))
        TextButton(
            onClick = onGoToSignIn,
            enabled = !loading
        ) {
            Text("Already have an account? Sign In")
        }
    }
}
