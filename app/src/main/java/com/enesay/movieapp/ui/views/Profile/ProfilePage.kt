package com.enesay.movieapp.ui.views.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.components.SimpleDialog
import com.enesay.movieapp.ui.theme.PrimaryBlack
import com.enesay.movieapp.ui.views.Auth.AuthState
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel
import com.enesay.movieapp.utils.DataStoreHelper
import com.enesay.movieapp.utils.setAppLocale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(navController: NavController) {

    var showDialog by remember { mutableStateOf(false) }
    val userPreferencesDataStore = DataStoreHelper(context = LocalContext.current)
    val loginViewmodel: LoginViewModel = hiltViewModel()
    val userInfo by loginViewmodel.userInfo
    val authState by loginViewmodel.authState
    val scope = rememberCoroutineScope()
    var isDarkMode = userPreferencesDataStore.darkModeFlow.collectAsState(initial = false).value
    var showSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewmodel.getUserInfo()
    }
    //val localeManager = remember { LocaleManager(context) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Profile", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary, titleContentColor = Color.White
            )
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 20.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp) // Add padding around the Row
                    .background(Color.Transparent), // Remove unnecessary solid background
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Section
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),// Add padding inside the card
                    elevation = CardDefaults.cardElevation(8.dp), // Add elevation for shadow
                    shape = RoundedCornerShape(16.dp) // Rounded corners for a modern look
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Image(
                            Icons.Default.Person, // Placeholder for profile image
                            contentDescription = "Profile Icon",
                            modifier = Modifier
                                .size(80.dp) // Larger size for profile picture
                                .clip(CircleShape) // Circular profile picture
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)) // Subtle background
                                .padding(16.dp)
                        )
                        SimpleDialog(
                            title = stringResource(R.string.txt_logout_title),
                            message = stringResource(R.string.txt_logout_message),
                            showDialog = showDialog,
                            onDismiss = { showDialog = false },
                            onConfirm = {
                                loginViewmodel.signOut()
                                if (authState is AuthState.Idle) {
                                    navController.navigate("login") {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                    scope.launch {
                                        userPreferencesDataStore.clearUserId()
                                    }
                                }
                                showDialog = false
                            },
                            negativeButtonText = stringResource(R.string.txt_cancel),
                            positiveButtonText = stringResource(R.string.txt_logout),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            if (!userInfo?.email.isNullOrEmpty()) {
                                Text(
                                    text = "${userInfo?.name} ${userInfo?.surname}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = userInfo?.email ?: stringResource(R.string.txt_visitor),
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            } else {
                                Text(
                                    text = userInfo?.email ?: stringResource(R.string.txt_visitor),
                                    fontSize = 19.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f), horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 75.dp)
            ) {
                item {
                    ProfileItemsRow(stringResource(R.string.txt_change_language), onClick = {
                        showSheet = true
                    })
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.txt_switch_dark_mode), fontSize = 20.sp)

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { isChecked ->
                                isDarkMode = isChecked
                                scope.launch {
                                    delay(400)
                                    userPreferencesDataStore.saveDarkModePreference(isDarkMode)
                                }
                            }
                        )
                    }
                }
                item {
                    if (loginViewmodel.currentUser.value != null) {
                        ProfileItemsRow("Log out", onClick = {
                            showDialog = true
                        }, color = Color.Red, showIcon = false)
                    }
                }

            }
            if (showSheet) {
                LanguageSelectionBottomSheet(
                    onLanguageSelected = { selectedLanguage ->
                        scope.launch {
                            userPreferencesDataStore.saveLanguagePreference(selectedLanguage.language)
                        }
                        setAppLocale(context, selectedLanguage.language)
                        showSheet = false

                    },
                    onDismissRequest = { showSheet = false },
                    currentLocale = Locale.getDefault()
                )
            }
        }
    }
}

@Composable
private fun ProfileItemsRow(
    title: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    showIcon: Boolean = true
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .padding(end = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 20.sp, color = color)
            if (showIcon) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "setting icon",
                    tint = color
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    currentLocale: Locale,
    onLanguageSelected: (Locale) -> Unit,
    onDismissRequest: () -> Unit
) {
    // Remember selected language
    var selectedLocale by remember { mutableStateOf(currentLocale) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.txt_select_language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val languages = listOf(
                stringResource(R.string.txt_english) to Locale("en"),
                stringResource(R.string.txt_turkish) to Locale("tr")
            )

            languages.forEach { language ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedLocale = language.second
                            onLanguageSelected(language.second)
                        }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = language.second == selectedLocale,
                        onClick = {
                            selectedLocale = language.second
                            onLanguageSelected(language.second)
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onPrimary)
                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = language.first,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}