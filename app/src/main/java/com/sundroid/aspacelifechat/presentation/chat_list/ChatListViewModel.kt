package com.sundroid.aspacelifechat.presentation.chat_list

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.sundroid.aspacelifechat.domain.use_case.GetConversations
import com.sundroid.aspacelifechat.domain.use_case.GetCurrentUser
import com.sundroid.aspacelifechat.domain.use_case.SignOut

import androidx.lifecycle.viewModelScope
import com.sundroid.aspacelifechat.domain.model.Conversation
import com.sundroid.aspacelifechat.domain.model.Response
import com.sundroid.aspacelifechat.domain.model.User
import com.sundroid.aspacelifechat.domain.use_case.CreateOrGetConversation
import com.sundroid.aspacelifechat.domain.use_case.DeleteAllConversations
import com.sundroid.aspacelifechat.domain.use_case.DeleteAllMessages
import com.sundroid.aspacelifechat.domain.use_case.GetAllUsers
import com.sundroid.aspacelifechat.domain.use_case.SynConversations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatListUiState(
    val conversations: List<Conversation> = emptyList(),
    val allUsers: List<User> = emptyList(),
    val isUserSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingUsers: Boolean = false,

    val error: String? = null,
    val isConfirmLogoutDialogVisible: Boolean = false
)

class ChatListViewModel(

    private val getConversations: GetConversations,
    private val getCurrentUser: GetCurrentUser,
    val signOut: SignOut,
    private val getAllUsers: GetAllUsers,
    private val createOrGetConversation: CreateOrGetConversation,
    private val synConversations: SynConversations,
    private val deleteConversations: DeleteAllConversations,
    private val deleteMessages: DeleteAllMessages
) : ViewModel(), Parcelable {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    constructor(parcel: Parcel) : this(
        TODO("getConversations"),
        TODO("getCurrentUser"),
        TODO("signOut"),
        TODO("getAllUsers"),
        TODO("createOrGetConversation"),
        TODO(""),
        TODO("deleteConversations"),
        TODO("deleteMessages")

    ) {
    }

    init {
        syncConversations()
        loadConversations()
    }

    private fun syncConversations(){
        val userId = currentUserId() ?: return


        viewModelScope.launch {

            synConversations(userId)
        }
    }
    private fun loadConversations() {
        viewModelScope.launch {

            val userId = getCurrentUser()?.uid
            if (userId == null) {
                _uiState.value = ChatListUiState(error = "User not logged in.")
                return@launch
            }

            getConversations(userId).collect { response ->

                when (response) {
                    is Response.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                    is Response.Success -> _uiState.value = ChatListUiState(conversations = response.data, isLoading = false)
                    is Response.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = response.message)
                }
            }
        }
    }

    fun onSignOut(navigate: () -> Unit) {
        viewModelScope.launch {
            signOut()
            deleteMessages()
            deleteConversations()
            navigate()
        }
    }
    fun onFabClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUsers = true) }
            when (val response = getAllUsers()) {
                is Response.Success -> {

                    val otherUsers = response.data.filter { it.userId != getCurrentUser()?.uid }
                    _uiState.update {
                        it.copy(
                            isLoadingUsers = false,
                            allUsers = otherUsers,
                            isUserSheetVisible = true
                        )
                    }
                }
                is Response.Error -> {
                    _uiState.update { it.copy(isLoadingUsers = false, error = response.message) }
                }
                is Response.Loading -> { /* Handled by isLoadingUsers */ }
            }
        }
    }
    fun onUserSelected(selectedUser: User, navigateToChat: (String, String) -> Unit) {
        viewModelScope.launch {
            if (getCurrentUser() == null) return@launch

            onDismissUserSheet()

            _uiState.update { it.copy(isLoading = true) }

            when (val response = createOrGetConversation(getCurrentUser()?.uid!!, selectedUser.userId)) {
                is Response.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    navigateToChat(response.data, selectedUser.displayName)

                }
                is Response.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = response.message) }
                }
                is Response.Loading -> { }
            }
        }
    }
    fun onDismissUserSheet() {
        _uiState.update { it.copy(isUserSheetVisible = false) }
    }
    fun currentUserId(): String?{
        return getCurrentUser()?.uid
    }
    fun currentUserName(): String{
        return getCurrentUser()?.displayName?: ""
    }

    fun showConfirmDialog(){
        _uiState.update { it.copy(isConfirmLogoutDialogVisible = true) }
    }
    fun hideConfirmDialog(){
        _uiState.update { it.copy(isConfirmLogoutDialogVisible = false) }
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatListViewModel> {
        override fun createFromParcel(parcel: Parcel): ChatListViewModel {
            return ChatListViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatListViewModel?> {
            return arrayOfNulls(size)
        }
    }
}