package com.sundroid.aspacelifechat.presentation.auth


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sundroid.aspacelifechat.presentation.auth.components.AuthButton
import com.sundroid.aspacelifechat.presentation.auth.components.DisplayNameTextField
import com.sundroid.aspacelifechat.presentation.auth.components.EmailTextField
import com.sundroid.aspacelifechat.presentation.auth.components.PasswordTextField
import org.koin.compose.viewmodel.koinViewModel
import com.sundroid.aspacelifechat.presentation.navigation.Screen


@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState = viewModel.authUiState
    var isLoginMode by remember { mutableStateOf(true) }


    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Screen.ChatList.route) {
                popUpTo(Screen.Auth.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) "Sign In" else "Sign Up",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(visible = !isLoginMode) {
            DisplayNameTextField(
                value = uiState.displayName,
                onValueChange = { viewModel.onEvent(AuthUiEvent.OnDisplayNameChange(it)) }
            )
        }
        EmailTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEvent(AuthUiEvent.OnEmailChange(it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = uiState.password,
            onValueChange = { viewModel.onEvent(AuthUiEvent.OnPasswordChange(it)) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthButton(
            text = if (isLoginMode) "Sign In" else "Sign Up",
            isLoading = uiState.isLoading,
            onClick = {
                if (isLoginMode) {
                    viewModel.onEvent(AuthUiEvent.OnSignIn)
                } else {
                    viewModel.onEvent(AuthUiEvent.OnSignUp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.clickable { isLoginMode = !isLoginMode },
            text = if (isLoginMode) "Don't have an account? Sign Up" else "Already have an account? Sign In",
            textAlign = TextAlign.Center
        )
    }


    uiState.isError?.let { error ->
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(key1 = error) {
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(AuthUiEvent.ClearError)
        }
        SnackbarHost(hostState = snackbarHostState)
    }
}