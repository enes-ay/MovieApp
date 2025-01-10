package com.enesay.movieapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleDialog(
    title: String,
    message: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    negativeButtonText: String,
    positiveButtonText: String,
    colorBtn: Color = Color.Red
) {
    if (showDialog) {
        AlertDialog (
            backgroundColor = MaterialTheme.colorScheme.primary,
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { onConfirm() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        ) {
                            Text(positiveButtonText, color = MaterialTheme.colorScheme.onPrimary)
                        }
                        OutlinedButton(
                            onClick = { onDismiss() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            Text(negativeButtonText, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp))
    }
}