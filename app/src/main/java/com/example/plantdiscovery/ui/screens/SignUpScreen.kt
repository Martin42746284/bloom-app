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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions


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
    onSignUpClick: (String, String) -> Unit,  // ✅ Retiré confirmPassword
    onGoogleClick: () -> Unit,
    onGoToSignIn: () -> Unit,
    onErrorDismiss: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }  // ✅pour validation locale
    var passwordVisible by remember { mutableStateOf(false) }  // ✅ Pour toggle password
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier
                .size(72.dp)
                .background(Color(0xFFE9F7EF), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🌱", fontSize = 36.sp)
        }
        Spacer(Modifier.height(36.dp))

        Text("Sign Up", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(18.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                localError = null
            },
            label = { Text("Email Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(14.dp))

        // Password Field avec toggle
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                localError = null
            },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        // Confirm Password Field avec toggle
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                localError = null
            },
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            }
        )

        Spacer(Modifier.height(18.dp))

        // Sign Up Button
        Button(
            onClick = {
                // ✅ Validation locale avant d'appeler onSignUpClick
                when {
                    email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        localError = "All fields are required"
                    }
                    password != confirmPassword -> {
                        localError = "Passwords do not match"
                    }
                    password.length < 6 -> {
                        localError = "Password must be at least 6 characters"
                    }
                    else -> {
                        localError = null
                        onSignUpClick(email, password)  // ✅ Seulement 2 paramètres maintenant
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Sign Up", color = Color.White)
            }
        }

        // Affichage des erreurs (locales ou du ViewModel)
        (error ?: localError)?.let { errorMessage ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (error != null) onErrorDismiss()
                        else localError = null
                    }
            )
        }

        Spacer(Modifier.height(10.dp))

        // Google Sign In Button
        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !loading
        ) {
            Text("Continue with Google")
        }

        Spacer(Modifier.height(18.dp))

        // Link to Sign In
        TextButton(
            onClick = onGoToSignIn,
            enabled = !loading
        ) {
            Text("Already have an account? Sign In")
        }
    }
}
