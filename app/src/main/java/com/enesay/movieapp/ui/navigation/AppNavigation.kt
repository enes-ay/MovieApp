package com.enesay.movieapp.ui.navigation

import PaymentPage
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.ui.views.AddCard.AddCardPage
import com.enesay.movieapp.ui.views.Auth.Login.LoginPage
import com.enesay.movieapp.ui.views.Cart.CartPage
import com.enesay.movieapp.ui.views.Favorites.FavoritesPage
import com.enesay.movieapp.ui.views.Home.HomePage
import com.enesay.movieapp.ui.views.MovieDetail.MovieDetailPage
import com.enesay.movieapp.ui.views.Profile.ProfilePage
import com.enesay.movieapp.ui.views.Splash.SplashPage
import com.enesay.movieapp.ui.views.Auth.Register.RegisterPage
import com.enesay.movieapp.ui.views.Home.MovieFilterPage
import com.google.common.reflect.TypeToken
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
            composable("favorites") {
                FavoritesPage(navController = navController)
            }
            composable("login") {
                LoginPage(navController = navController)
            }
            composable("register") {
                RegisterPage(navController = navController)
            }

            composable(
                route = "payment?groupedMovies={cartItems}",
                arguments = listOf(navArgument("cartItems") { type = NavType.StringType })
            ) { backStackEntry ->
                val cartItemsJson = backStackEntry.arguments?.getString("cartItems")
                val groupedMovies: List<MovieCart> = Gson().fromJson(
                    cartItemsJson,
                    object : TypeToken<List<MovieCart>>() {}.type
                )

                PaymentPage(navController = navController, cartItems = groupedMovies)
            }

            composable("profile") {
                ProfilePage(navController = navController)
            }
            composable("movieFilter") {
                MovieFilterPage(navController)
            }
            composable("addCard") {
                AddCardPage(navController)
            }
            composable("movieDetail/{movie}") {
                val movie = it.arguments?.getString("movie")
                val movieObject = Gson().fromJson(movie, Movie::class.java)
                MovieDetailPage(navController = navController, movieObject)
            }
        }
    }