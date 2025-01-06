package com.enesay.movieapp.ui.auth.Login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.common.Resource
import com.enesay.movieapp.data.model.User
import com.enesay.movieapp.data.repository.AuthRepository
import com.enesay.movieapp.ui.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) :ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    val userLoggedIn = mutableStateOf(authRepository.isUserLoggedIn())
    val currentUser = mutableStateOf(authRepository.getCurrentUserId())

    private val _userInfo = mutableStateOf<User?>(null)
    val userInfo: State<User?> = _userInfo

    init {
        userLoggedIn.value= authRepository.isUserLoggedIn()
    }

    // Sing In logic
    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password cannot be null")
        } else {
            val result = authRepository.signIn(email, password)
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Authenticated
                is Resource.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }

    // Sign out logic
    fun signOut() = viewModelScope.launch {
        val result = authRepository.signOut()
        _authState.value = when (result) {
            is Resource.Success -> {
                userLoggedIn.value = false
                AuthState.Idle
            }
            is Resource.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
        }
    }

    fun getUserInfo() = viewModelScope.launch {
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            when (val result = authRepository.getUserProfile(userId)) {
                is Resource.Success -> _userInfo.value = result.data
                is Resource.Error -> {
                    AuthState.Error(result.exception.message ?: "User Info Error")
                }
            }
        }
    }
}