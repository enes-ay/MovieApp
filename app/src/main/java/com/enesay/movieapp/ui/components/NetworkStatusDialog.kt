package com.enesay.movieapp.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.enesay.movieapp.R
import com.enesay.movieapp.utils.NetworkViewModel
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusDialog() {
    val networkViewModel: NetworkViewModel = hiltViewModel()
    val isConnected by networkViewModel.isConnected.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        delay(2000)
    }

    SimpleDialog(
        title = stringResource(R.string.txt_no_internet_connection),
        message = stringResource(R.string.txt_no_internet_connection_message),
        onConfirm = {
            networkViewModel.retryConnectionStatus()
        },
        negativeButtonText = stringResource(R.string.txt_cancel),
        positiveButtonText = stringResource(R.string.txt_retry),
        showDialog = !isConnected,
        onDismiss = {
            (context as Activity).finishAffinity()
        }
    )
}
