package com.enesay.movieapp.ui.views.Auth.Register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.common.Resource
import com.enesay.movieapp.data.repository.AuthRepository
import com.enesay.movieapp.ui.views.Auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository)
    : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun signUp(email:String, password:String, name:String, surname:String) = viewModelScope.launch{
        _authState.value = AuthState.Loading

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or Password cannot be null")
        }
        else{
            val result = authRepository.signUp(email, password, name, surname)
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Authenticated
                is Resource.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }
}