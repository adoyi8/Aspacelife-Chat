package com.sundroid.aspacelifechat.auth.presentation.navigation



sealed class Screen(val route: String) {
    object Auth : Screen("auth_screen")
    object ChatList : Screen("chat_list_screen")
    object Chat : Screen("chat_screen/{conversationId}?otherUserDisplayName={otherUserDisplayName}") {
        fun createRoute(conversationId: String, otherUserDisplayName: String): String {
            return "chat_screen/$conversationId?otherUserDisplayName=$otherUserDisplayName"
        }
    }
}