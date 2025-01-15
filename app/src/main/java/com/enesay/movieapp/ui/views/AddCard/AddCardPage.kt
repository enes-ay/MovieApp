package com.enesay.movieapp.ui.views.AddCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardPage(navController: NavHostController) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val addCardViewModel = hiltViewModel<AddCardViewModel>()
    val currentUser = loginViewModel.currentUser.value
    val uiState by addCardViewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.txt_add_new_card),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TextInputField(
                value = uiState.cardNumber,
                onValueChange = { addCardViewModel.onEvent(AddCardEvent.CardNumberChanged(it)) },
                label = "Card Number",
                error = uiState.cardNumberError
            )
            TextInputField(
                value = uiState.cardHolderName,
                onValueChange = { addCardViewModel.onEvent(AddCardEvent.CardHolderNameChanged(it)) },
                label = "Name on Card",
                error = uiState.cardHolderNameError
            )

            TextInputField(
                value = uiState.expiryDate,
                onValueChange = { addCardViewModel.onEvent(AddCardEvent.ExpiryDateChanged(it)) },
                label = "Expiry Date (MM/YY)",
                error = uiState.expiryDateError,
            )
            TextInputField(
                value = uiState.cvv,
                onValueChange = { addCardViewModel.onEvent(AddCardEvent.CvvChanged(it)) },
                label = "CVV",
                error = uiState.cvvError,
            )
            Spacer(modifier = Modifier.width(10.dp))


            Button(
                onClick = {
                    addCardViewModel.onEvent(AddCardEvent.SaveCard)
                    if (uiState.isSaved) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(
                    text = "Save Card",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(
                    text = stringResource(R.string.txt_cancel),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }
        }
    }
}


@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}