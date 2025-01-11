package com.enesay.movieapp.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.enesay.movieapp.R
import com.enesay.movieapp.utils.Constants.GOOGLE_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

@Composable
fun GoogleLoginButton(
    onGetCredentialResponse: (Credential) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    Button(modifier = Modifier.wrapContentWidth().padding(top = 20.dp).border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(GOOGLE_CLIENT_ID)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )
                    onGetCredentialResponse(result.credential)
                } catch (e: GetCredentialException) {
                    Log.e("credentialError", e.message.orEmpty())
                }
            }

        }) {
      Row(modifier = Modifier.wrapContentWidth(), horizontalArrangement = Arrangement.spacedBy(13.dp),
          verticalAlignment = Alignment.CenterVertically) {
          Image(modifier = Modifier.size(24.dp), painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = "Google Logo")
          Text(stringResource(id = R.string.txt_signUp_with_google), fontSize = 18.sp, fontWeight = FontWeight.Medium)
      }
    }

}