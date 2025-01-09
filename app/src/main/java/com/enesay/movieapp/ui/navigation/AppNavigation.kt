package com.enesay.movieapp.ui.navigation

import PaymentPage
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.ui.views.Auth.Login.LoginPage
import com.enesay.movieapp.ui.views.Cart.CartPage
import com.enesay.movieapp.ui.views.Favorites.FavoritesPage
import com.enesay.movieapp.ui.views.Home.HomePage
import com.enesay.movieapp.ui.views.MovieDetail.MovieDetailPage
import com.enesay.movieapp.ui.views.Profile.ProfilePage
import com.enesay.movieapp.ui.views.Splash.SplashPage
import com.enesay.movieapp.ui.views.Auth.Register.RegisterPage
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues){

        NavHost(navController = navController, startDestination = "home") {

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
                RegisterPage(navController = navController)
            }
            composable("payment") {
                PaymentPage(navController = navController)
            }
            composable("profile") {
                ProfilePage(navController = navController)
            }
            composable("movieDetail/{movie}") {
                val movie = it.arguments?.getString("movie")
                val movieObject = Gson().fromJson(movie, Movie::class.java)
                MovieDetailPage(navController = navController, movieObject)
            }
        }
    }