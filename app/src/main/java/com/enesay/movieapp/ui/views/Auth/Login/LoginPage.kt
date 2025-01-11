package com.enesay.movieapp.ui.views.Auth.Login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.components.CustomTextField
import com.enesay.movieapp.ui.views.Auth.AuthState
import com.enesay.movieapp.ui.components.SimpleOutlinedButton
import com.enesay.movieapp.utils.DataStoreHelper
import com.enesay.movieapp.utils.ValidationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    val loginViewmodel: LoginViewModel = hiltViewModel()
    val authState by loginViewmodel.authState
    val userPreferencesDataStore = DataStoreHelper(context = LocalContext.current)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        val rememberMe = userPreferencesDataStore.rememberMeFlow.collectAsState(initial = false)
        val email = userPreferencesDataStore.emailFlow.collectAsState(initial = "")
        val password = userPreferencesDataStore.passwordFlow.collectAsState(initial = "")
        // Input value's states
        val emailState = rememberSaveable { mutableStateOf(email.value ?: "") }
        val passwordState = rememberSaveable { mutableStateOf(password.value ?: "") }
        val rememberMeState = rememberSaveable { mutableStateOf(rememberMe.value) }

        val emailError = rememberSaveable { mutableStateOf<String?>(null) }
        val passwordError = rememberSaveable { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = rememberMe.value, key2 = email.value, key3 = password.value) {
            emailState.value = email.value ?: ""
            passwordState.value = password.value ?: ""
            rememberMeState.value = rememberMe.value
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 20.dp),
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    // E-mail field
                    CustomTextField(
                        value = emailState.value,
                        onValueChange = {
                            emailState.value = it
                            emailError.value = null
                        },
                        label = "Email",
                        isError = emailError.value != null,
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = emailError.value
                    )
                    // Password Field
                    CustomTextField(
                        value = passwordState.value,
                        onValueChange = {
                            passwordState.value = it
                            passwordError.value = null
                        },
                        label = "Password",
                        isError = passwordError.value != null,
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = passwordError.value
                    )
                    // Remember me checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                            checked = rememberMeState.value,
                            onCheckedChange = {
                                rememberMeState.value = it
                            })
                        Text(
                            "Remember me",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    when (authState) {
                        is AuthState.Idle ->
                            SimpleOutlinedButton(
                                text = stringResource(R.string.txt_login),
                                onClick = {
                                    emailError.value = ValidationUtils.validateEmail(emailState.value)

                                    if (passwordState.value.isBlank()) {
                                        passwordError.value = "Password cannot be empty"
                                    }
                                    if (emailError.value == null && passwordError.value == null) {
                                        loginViewmodel.signIn(emailState.value, passwordState.value)
                                    }
                                },
                            )

                        is AuthState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)

                        is AuthState.Authenticated -> {
                            LaunchedEffect(AuthState.Authenticated) {
                                scope.launch {
                                    if (rememberMeState.value) {
                                        userPreferencesDataStore.saveUserPreferences(
                                            rememberMeState.value,
                                            emailState.value,
                                            passwordState.value
                                        )
                                    }
                                    loginViewmodel.currentUser.value?.let {
                                        userPreferencesDataStore.saveUserId(
                                            it
                                        )
                                    }
                                }
                            }

                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }

                        is AuthState.Error -> {
                            Text(
                                text = (authState as AuthState.Error).message,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                            SimpleOutlinedButton(
                                text = stringResource(R.string.txt_retry),
                                onClick = {
                                    loginViewmodel.signIn(
                                        emailState.value, passwordState.value
                                    )
                                },
                                borderColor = MaterialTheme.colorScheme.error,
                                textColor = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Absolute.Center
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " Create here!",
                            modifier = Modifier.clickable { navController.navigate("register") },
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                    }
                    // Continue without register button
                    TextButton(
                        onClick = {
                            navController.navigate("home")
                        },
                        shape = RoundedCornerShape(22.dp), // Modern rounded corners
                        border = BorderStroke(
                            1.2.dp,
                            MaterialTheme.colorScheme.primary
                        ), // Customize border
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.4f
                            ),
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .padding(top = 20.dp)
                            .wrapContentSize()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onPrimary,
                                RoundedCornerShape(22.dp)
                            )
                        // Fixed height for consistency
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = stringResource(R.string.txt_continue_without_register),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}