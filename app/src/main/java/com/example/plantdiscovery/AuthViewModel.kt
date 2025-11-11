package com.example.plantdiscovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantdiscovery.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _successSignUp = MutableStateFlow(false)
    val successSignUp = _successSignUp.asStateFlow()

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Email and password are required."
            return
        }
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            _loading.value = false
            result.onSuccess { onSuccess() }
                .onFailure { _error.value = it.message ?: "Erreur d'authentification" }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _error.value = "All fields are required."
            return
        }
        if (password != confirmPassword) {
            _error.value = "Passwords do not match."
            return
        }
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            _loading.value = false
            result.onSuccess { _successSignUp.value = true }
                .onFailure { _error.value = it.message ?: "Erreur de création de compte" }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearSignUpSuccess() {
        _successSignUp.value = false
    }
}
