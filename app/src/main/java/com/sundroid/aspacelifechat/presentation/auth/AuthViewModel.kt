package com.sundroid.aspacelifechat.presentation.auth

import com.sundroid.aspacelifechat.domain.model.Response
import com.sundroid.aspacelifechat.domain.use_case.GetCurrentUser
import com.sundroid.aspacelifechat.domain.use_case.SignIn
import com.sundroid.aspacelifechat.domain.use_case.SignOut
import com.sundroid.aspacelifechat.domain.use_case.SignUp



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(
    private val getCurrentUser: GetCurrentUser,
    private val signIn: SignIn,
    private val signUp: SignUp,
    private val signOut: SignOut
) : ViewModel() {

    var authUiState by mutableStateOf(AuthUiState())
        private set

    val currentUser: FirebaseUser?
        get() = getCurrentUser()

    init {
        if (currentUser != null) {
            authUiState = authUiState.copy(isSuccess = true)
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            if(authUiState.email.isBlank() || authUiState.password.isBlank()){
                authUiState = authUiState.copy(isLoading = false, isError = "Enter valid email and password")
                return@launch
            }
            signIn(authUiState.email, authUiState.password).collect { response ->
                when (response) {

                    is Response.Loading -> authUiState = authUiState.copy(isLoading = true)
                    is Response.Success -> {
                        authUiState = authUiState.copy(isLoading = false, isSuccess = true)
                    }
                    is Response.Error -> {
                        authUiState = authUiState.copy(isLoading = false, isError = response.message)
                    }
                }
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            if(authUiState.email.isBlank()){
                authUiState = authUiState.copy(isLoading = false, isError = "Enter valid email")
                return@launch
            }
            else if(authUiState.password.isBlank()){
                authUiState = authUiState.copy(isLoading = false, isError = "Enter valid password")
                return@launch
            }
            else if(authUiState.displayName.isBlank() || authUiState.displayName.length < 3){
                authUiState = authUiState.copy(isLoading = false, isError = "Enter valid Name")
                return@launch
            }
            signUp(authUiState.email, authUiState.password, authUiState.displayName).collect { response ->

                when (response) {

                    is Response.Loading -> authUiState = authUiState.copy(isLoading = true)
                    is Response.Success -> {
                        authUiState = authUiState.copy(isLoading = false, isSuccess = true)

                    }
                    is Response.Error -> {
                        authUiState = authUiState.copy(isLoading = false, isError = response.message)
                    }
                }
            }
        }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnEmailChange -> {
                authUiState = authUiState.copy(email = event.email)
            }
            is AuthUiEvent.OnPasswordChange -> {
                authUiState = authUiState.copy(password = event.password)
            }
            is AuthUiEvent.OnDisplayNameChange -> {
                authUiState = authUiState.copy(displayName = event.displayName)
            }
            is AuthUiEvent.OnSignIn -> {
                signIn()
            }
            is AuthUiEvent.OnSignUp -> {
                signUp()
            }
            is AuthUiEvent.ClearError -> {
                authUiState = authUiState.copy(isError = null)
            }
        }
    }
}