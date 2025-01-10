package com.enesay.movieapp.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.components.SimpleDialog
import com.enesay.movieapp.ui.theme.PrimaryBlack
import java.util.Locale

fun setAppLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

@Composable
fun ShowLoginWarningDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        SimpleDialog(
            title = stringResource(R.string.txt_login_required_title),
            message = stringResource(R.string.txt_login_required_message),
            negativeButtonText = stringResource(R.string.txt_cancel),
            positiveButtonText = stringResource(R.string.txt_login),
            showDialog = showDialog,
            onDismiss = {
                onDismiss()
            },
            onConfirm = {
                onConfirm()
            },
            colorBtn = PrimaryBlack
        )
    }
}