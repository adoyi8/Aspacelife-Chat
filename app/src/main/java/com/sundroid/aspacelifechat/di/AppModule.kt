package com.sundroid.aspacelifechat.di

import androidx.room.Room
import com.sundroid.aspacelifechat.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sundroid.aspacelifechat.data.local.ChatDatabase
import com.sundroid.aspacelifechat.data.repository.ChatRepositoryImpl
import com.sundroid.aspacelifechat.data.repository.UserRepositoryImpl
import com.sundroid.aspacelifechat.domain.repository.AuthRepository
import com.sundroid.aspacelifechat.domain.repository.ChatRepository
import com.sundroid.aspacelifechat.domain.repository.UserRepository
import com.sundroid.aspacelifechat.domain.use_case.CreateOrGetConversation
import com.sundroid.aspacelifechat.domain.use_case.DeleteAllConversations
import com.sundroid.aspacelifechat.domain.use_case.DeleteAllMessages
import com.sundroid.aspacelifechat.domain.use_case.GetAllUsers
import com.sundroid.aspacelifechat.domain.use_case.GetConversations
import com.sundroid.aspacelifechat.domain.use_case.GetCurrentUser
import com.sundroid.aspacelifechat.domain.use_case.GetMessages
import com.sundroid.aspacelifechat.domain.use_case.GetUser
import com.sundroid.aspacelifechat.domain.use_case.SendMessage
import com.sundroid.aspacelifechat.domain.use_case.SignIn
import com.sundroid.aspacelifechat.domain.use_case.SignOut
import com.sundroid.aspacelifechat.domain.use_case.SignUp
import com.sundroid.aspacelifechat.domain.use_case.SynConversations
import com.sundroid.aspacelifechat.presentation.auth.AuthViewModel
import com.sundroid.aspacelifechat.presentation.chat.ChatViewModel
import com.sundroid.aspacelifechat.presentation.chat_list.ChatListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Room Database
    single {
        Room.databaseBuilder(
                androidApplication(),
                ChatDatabase::class.java,
                "chat_database"
            ).fallbackToDestructiveMigration(true).build()
    }
    single { get<ChatDatabase>().conversationDao() }
    single { get<ChatDatabase>().messageDao() }


    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ChatRepository> { ChatRepositoryImpl(get(), get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    // Use Cases
    single { GetCurrentUser(get()) }
    single { SignIn(get()) }
    single { SignUp(get()) }
    single { SignOut(get()) }
    single { GetConversations(get()) }
    single { GetMessages(get()) }
    single { SendMessage(get()) }
    single { GetUser(get()) }
    single { GetAllUsers(get()) }
    single { CreateOrGetConversation(get()) }
    single { SynConversations(get()) }
    single { DeleteAllConversations(get()) }
    single { DeleteAllMessages(get()) }



    // ViewModels
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { ChatListViewModel(get(), get(), get(), get(), get(),get(), get(), get()) }
    viewModel { ChatViewModel(get(), get(), get(), get()) }
}