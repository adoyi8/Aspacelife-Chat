package com.sundroid.aspacelifechat.presentation.auth


data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = null
)


sealed interface AuthUiEvent {
    data class OnEmailChange(val email: String) : AuthUiEvent
    data class OnPasswordChange(val password: String) : AuthUiEvent
    data class OnDisplayNameChange(val displayName: String) : AuthUiEvent
    object OnSignIn : AuthUiEvent
    object OnSignUp : AuthUiEvent
    object ClearError: AuthUiEvent
}