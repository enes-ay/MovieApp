package com.enesay.movieapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SimpleOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(22.dp),
    borderColor: Color = MaterialTheme.colorScheme.onPrimary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    fontSize: TextUnit = 19.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    height: Dp = 45.dp
) {
    OutlinedButton (
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        border = BorderStroke(1.2.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            disabledContentColor = textColor.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(height)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}