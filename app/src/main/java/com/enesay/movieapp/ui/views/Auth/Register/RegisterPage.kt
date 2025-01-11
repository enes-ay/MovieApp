package com.enesay.movieapp.ui.views.Auth.Register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.views.Auth.AuthState
import com.enesay.movieapp.ui.components.CustomTextField
import com.enesay.movieapp.ui.components.GoogleLoginButton
import com.enesay.movieapp.ui.components.SimpleOutlinedButton


@Composable
fun RegisterPage(navController: NavController) {

    val registerViewmodel: RegisterViewModel = hiltViewModel()
    val authState by registerViewmodel.authState

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }
        val name = rememberSaveable { mutableStateOf("") }
        val surname = rememberSaveable { mutableStateOf("") }
        val emailError = rememberSaveable { mutableStateOf<String?>(null) }
        val passwordError = rememberSaveable { mutableStateOf<String?>(null) }
        val nameError = rememberSaveable { mutableStateOf<String?>(null) }
        val surnameError = rememberSaveable { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 30.dp),
                    text = stringResource(R.string.txt_register),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                CustomTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                        nameError.value = null
                    },
                    label = "Name",
                    isError = nameError.value != null,
                    errorMessage = nameError.value,
                )

                CustomTextField(
                    value = surname.value,
                    onValueChange = {
                        surname.value = it
                        surnameError.value = null
                    },
                    label = "Surname",
                    isError = surnameError.value != null,
                    errorMessage = surnameError.value
                )

                CustomTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        emailError.value = null
                    },
                    label = "Email",
                    isError = emailError.value != null,
                    errorMessage = emailError.value
                )

                CustomTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        passwordError.value = null
                    },
                    label = "Password",
                    isError = passwordError.value != null,
                    errorMessage = passwordError.value
                )
                Spacer(modifier = Modifier.size(23.dp))

                // Auth State Handling
                when (authState) {
                    is AuthState.Idle ->
                        SimpleOutlinedButton(
                            text = stringResource(R.string.txt_register),
                            onClick = {
                                val errors = registerViewmodel.validateInputs(name.value, surname.value, email.value, password.value)
                                nameError.value = errors["name"]
                                surnameError.value = errors["surname"]
                                emailError.value = errors["email"]
                                passwordError.value = errors["password"]

                                if (emailError.value == null && passwordError.value == null) {
                                    registerViewmodel.signUp(
                                        email.value,
                                        password.value,
                                        name.value,
                                        surname.value
                                    )
                                }
                            },
                        )

                    is AuthState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)

                    is AuthState.Authenticated -> {
                        navController.navigate("home") {
                            popUpTo("register") {
                                inclusive = true
                            }
                        }
                    }

                    is AuthState.Error -> {
                        Text(text = (authState as AuthState.Error).message, color = Color.Red)

                        SimpleOutlinedButton(
                            text = stringResource(R.string.txt_retry),
                            onClick = {
                                registerViewmodel.signUp(
                                    email.value,
                                    password.value,
                                    name.value,
                                    surname.value
                                )
                            },
                            borderColor = MaterialTheme.colorScheme.error,
                            textColor = MaterialTheme.colorScheme.error
                        )
                    }
                }
                // Google login button

                GoogleLoginButton(
                    onGetCredentialResponse = { credential ->
                        registerViewmodel.signInWithGoogle(credential)
                    }
                )
            }
        }
    }
}
