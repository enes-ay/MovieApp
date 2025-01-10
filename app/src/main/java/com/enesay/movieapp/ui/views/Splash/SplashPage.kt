package com.enesay.movieapp.ui.views.Splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navController: NavController) {
    val loginViewmodel: LoginViewModel = hiltViewModel()

    // Load the Lottie animation from Asset directory
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("splash_animation.json"))

    LaunchedEffect(Unit) {
        val isLoggedIn = loginViewmodel.userLoggedIn.value
        delay(2000L)
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LottieAnimation(
            composition = composition,
            iterations = 2,
        )
    }
}