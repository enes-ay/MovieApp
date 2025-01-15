package com.enesay.movieapp.ui.views.Favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enesay.movieapp.R
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel
import com.enesay.movieapp.ui.views.Home.MovieCard
import com.enesay.movieapp.utils.ShowLoginWarningDialog
import com.google.gson.Gson
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(navController: NavController) {
    val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val favoriteMovies by favoritesViewModel.favoriteMovies.observeAsState()
    val isFavorite by remember { mutableStateOf(true) }
    var showLoginWarningDialog by remember { mutableStateOf(false) }
    val isLoggedIn = loginViewModel.userLoggedIn.value
    val currentUserId = loginViewModel.currentUser.value

    LaunchedEffect(true) {
        if (isLoggedIn.not()) {
            Log.d("favorites", "kullanıcı $isLoggedIn")
            showLoginWarningDialog = true
        } else {
            currentUserId?.let { favoritesViewModel.getFavoriteMovies(it) }
        }
        Log.d("favorites", "fav compose $favoriteMovies")
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.txt_favs),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }

    ) { paddingValues ->
        if (favoriteMovies.isNullOrEmpty()) {
            ShowLoginWarningDialog(showDialog = showLoginWarningDialog,
                onDismiss = {
                    showLoginWarningDialog = false
                    navController.navigate("home") {
                        popUpTo("favorites") {
                            inclusive = true
                        }
                    }
                },
                onConfirm = {
                    navController.navigate("login") {
                        popUpTo("favorites") {
                            inclusive = true
                        }
                    }
                    showLoginWarningDialog = false
                })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.txt_no_movies),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                )
            }
        } else {

            ShowLoginWarningDialog(showDialog = showLoginWarningDialog,
                onDismiss = {
                    showLoginWarningDialog = false
                    navController.navigate("home") {
                        popUpTo("favorites") {
                            inclusive = true
                        }
                    }
                },
                onConfirm = {
                    navController.navigate("login"){
                        popUpTo("favorites"){
                            inclusive = true
                        }
                    }
                    showLoginWarningDialog = false
                })

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    bottom = WindowInsets.systemBars.asPaddingValues()
                        .calculateBottomPadding() + 56.dp // Padding for bottom bar
                )
            ) {
                favoriteMovies?.let { movies ->
                    items(items = movies, key = { movie -> movie.id }) { movie ->
                        Log.d("movies", "${movies} ")
                        MovieCard(movie = movie,
                            onFavoriteClick = {
                                if (currentUserId != null) {
                                    Log.d("fav", "user id null değil $currentUserId")
                                    favoritesViewModel.removeFavoriteMovie(
                                        userId = currentUserId,
                                        it
                                    )
                                    Log.d("movie", it.toString())
                                }
                                true
                            }, onClick = {
                                val movieJson = Gson().toJson(movie)
                                navController.navigate("movieDetail/${movieJson}")
                            },
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }
}