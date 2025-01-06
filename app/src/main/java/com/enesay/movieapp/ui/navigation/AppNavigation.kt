package com.enesay.movieapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.enesay.financialliteracy.ui.presentation.Register.Register
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.ui.views.Cart.CartPage
import com.enesay.movieapp.ui.views.Favorites.FavoritesPage
import com.enesay.movieapp.ui.views.Home.HomePage
import com.enesay.movieapp.ui.auth.Login.LoginPage
import com.enesay.movieapp.ui.views.MovieDetail.MovieDetailPage
import com.enesay.movieapp.ui.views.Splash.SplashPage
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues){

        NavHost(navController = navController, startDestination = "login") {

            composable("splash") {
                SplashPage(navController = navController, paddingValues)
            }
            composable("home") {
                HomePage(navController = navController)
            }
            composable("cart") {
                CartPage(navController = navController)
            }
            composable("favorites") {
                FavoritesPage(navController = navController)
            }
            composable("login") {
                LoginPage(navController = navController)
            }
            composable("register") {
                Register(navController = navController)
            }
            composable("movieDetail/{movie}") {
                val movie = it.arguments?.getString("movie")
                val movieObject = Gson().fromJson(movie, Movie::class.java)
                MovieDetailPage(navController = navController, movieObject)
            }
        }
    }