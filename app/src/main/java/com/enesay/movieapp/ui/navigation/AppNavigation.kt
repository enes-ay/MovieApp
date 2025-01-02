package com.enesay.movieapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.enesay.movieapp.ui.views.CartPage
import com.enesay.movieapp.ui.views.HomePage
import com.enesay.movieapp.ui.views.SplashPage
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues){

        NavHost(navController = navController, startDestination = "splash") {

            composable("splash") {
                SplashPage(navController = navController)
            }
            composable("home") {
                HomePage(navController = navController)
            }
            composable("cart") {
                CartPage(navController = navController)
            }
        }
    }