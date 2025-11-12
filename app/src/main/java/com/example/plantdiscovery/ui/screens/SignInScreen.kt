package com.example.plantdiscovery.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign


//@Preview(showBackground = true)
//@Composable
//fun SignInScreenPreview() {
//    SignInScreen(
//        onSignInClick = { email, password -> /* mock */ },
//        onGoogleClick = {},
//        onGoToSignUp = {}
//    )
//}

@Composable
fun SignInScreen(
    loading: Boolean,
    error: String?,
    onSignInClick: (String, String) -> Unit,
    onGoogleClick: () -> Unit,
    onGoToSignUp: () -> Unit,
    onErrorDismiss: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }  // ✅ Validation locale
    var passwordVisible by remember { mutableStateOf(false) }  // ✅ Toggle password
    val focusManager = LocalFocusManager.current  // ✅ Pour gérer le focus

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

        Text("Sign In", style = MaterialTheme.typography.titleLarge)
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
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = (error != null || localError != null) && email.isBlank()
        )

        Spacer(Modifier.height(14.dp))

        // Password Field avec toggle de visibilité
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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onSignInClick(email, password)
                    } else {
                        localError = "Email and password are required"
                    }
                }
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
            },
            isError = (error != null || localError != null) && password.isBlank()
        )

        Spacer(Modifier.height(24.dp))

        // Sign In Button avec validation
        Button(
            onClick = {
                when {
                    email.isBlank() && password.isBlank() -> {
                        localError = "Email and password are required"
                    }
                    email.isBlank() -> {
                        localError = "Email is required"
                    }
                    password.isBlank() -> {
                        localError = "Password is required"
                    }
                    else -> {
                        localError = null
                        onSignInClick(email, password)
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
                Text("Sign In", color = Color.White)
            }
        }

        // Affichage des erreurs (locales ou du ViewModel)
        (error ?: localError)?.let { errorMessage ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
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

        // Link to Sign Up
        TextButton(
            onClick = onGoToSignUp,
            enabled = !loading
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}

