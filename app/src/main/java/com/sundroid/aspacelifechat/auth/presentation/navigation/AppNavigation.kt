package com.sundroid.aspacelifechat.auth.presentation.navigation

import com.sundroid.aspacelifechat.auth.presentation.auth.AuthScreen
import com.sundroid.aspacelifechat.auth.presentation.auth.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel



import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sundroid.aspacelifechat.auth.presentation.chat.ChatScreen
import com.sundroid.aspacelifechat.auth.presentation.chat_list.ChatListScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val startDestination = if (authViewModel.currentUser != null) {
        Screen.ChatList.route
    } else {
        Screen.Auth.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.ChatList.route) {
            ChatListScreen(navController = navController)
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            val otherUserDisplayName = backStackEntry.arguments?.getString("otherUserDisplayName") ?: ""

            ChatScreen(conversationId = conversationId, otherUserDisplayName = otherUserDisplayName, navController = navController)
        }
    }
}