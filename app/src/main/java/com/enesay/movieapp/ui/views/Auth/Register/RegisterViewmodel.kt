package com.enesay.movieapp.ui.views.Auth.Register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.common.Resource
import com.enesay.movieapp.data.repository.AuthRepository
import com.enesay.movieapp.ui.views.Auth.AuthState
import com.enesay.movieapp.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    fun signUp(email: String, password: String, name: String, surname: String) =
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or Password cannot be null")
            } else {
                val result = authRepository.signUp(email, password, name, surname)
                _authState.value = when (result) {
                    is Resource.Success -> AuthState.Authenticated
                    is Resource.Error -> AuthState.Error(
                        result.exception.message ?: "Unknown error"
                    )
                }
            }
        }

    fun validateInputs(
        name: String,
        surname: String,
        email: String,
        password: String
    ): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()

        errors["name"] = ValidationUtils.validateName(name)
        errors["surname"] = ValidationUtils.validateSurname(surname)
        errors["email"] = ValidationUtils.validateEmail(email)
        errors["password"] = ValidationUtils.validatePassword(password)

        return errors
    }

    fun signInWithGoogle(credential: Credential) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signInWithGoogle(credential)
            _authState.value = when (result) {
                is Resource.Success -> AuthState.Authenticated
                is Resource.Error -> AuthState.Error(result.exception.message ?: "Unknown error")
            }
        }
    }
}