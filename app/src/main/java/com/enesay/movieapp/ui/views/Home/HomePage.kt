package com.enesay.movieapp.ui.views.Home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.ui.theme.PrimaryBlack
import com.enesay.movieapp.ui.theme.PrimaryWhite
import com.enesay.movieapp.ui.views.Auth.Login.LoginViewModel
import com.enesay.movieapp.ui.views.Favorites.FavoritesViewModel
import com.enesay.movieapp.utils.Constants
import com.enesay.movieapp.utils.ShowLoginWarningDialog
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val isLoggedIn = loginViewModel.userLoggedIn.value
    val currentUserId = loginViewModel.currentUser.value
    val moviesState by homeViewModel.moviesState
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current  // search focus kontrol
    var isFocused by remember { mutableStateOf(false) }
    val sortOption by homeViewModel.sortOption.collectAsState()
    var isSortBottomSheetVisible by remember { mutableStateOf(false) }
    val favList by favoritesViewModel.favoriteMovies.observeAsState()
    var showLoginWarningDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        homeViewModel.getMovies()
        currentUserId?.let { favoritesViewModel.getFavoriteMovies(it) }
        Log.d("home", "$currentUserId")
        Log.d("launcg", "home")
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            // Top App Bar with Search and Filter Row
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                // Top App Bar
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlack)
                )

                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(text = stringResource(id = R.string.txt_seach)) },
                    leadingIcon = {
                        if (isFocused) {
                            IconButton(onClick = {
                                searchQuery = ""
                                focusManager.clearFocus() // Closes the keyboard and exits search
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back Icon"
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search, // Displays search icon
                                contentDescription = "Search Icon"
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp, // Border width
                            color = if (isFocused) Color.Blue else Color.Gray, // Border color based on focus
                            shape = RoundedCornerShape(8.dp) // Matches the TextField's shape
                        )
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused // Tracks focus state
                        }
                )

                // Filter & Sort Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Sort Button
                    Button(
                        onClick = {
                            isSortBottomSheetVisible = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlack),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_sort),
                                contentDescription = "Sort Icon",
                                tint = PrimaryWhite
                            )
                            // Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.txt_sort),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryWhite
                            )
                        }
                    }
                    // Filter Button
                    Button(
                        onClick = {
                            // Handle filter logic
                            navController.navigate("movieFilter")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlack),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter),
                                contentDescription = "Filter Icon",
                                tint = PrimaryWhite
                            )
                            // Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.txt_filter),
                                color = PrimaryWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            ShowLoginWarningDialog(showDialog = showLoginWarningDialog,
                onDismiss = {
                    showLoginWarningDialog = false
                },
                onConfirm = {
                    navController.navigate("login"){
                        popUpTo("home")
                    }
                    showLoginWarningDialog = false
                })

            when (moviesState) {
                is MoviesUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                is MoviesUiState.Success -> {
                    val filteredMovieList =
                        (moviesState as MoviesUiState.Success).movies.filter { movie ->
                            movie.name.contains(searchQuery, ignoreCase = true)
                        }
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            bottom = WindowInsets.systemBars.asPaddingValues()
                                .calculateBottomPadding() + 56.dp
                        )
                    ) {
                        items(filteredMovieList) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = {
                                    val movieJson = Gson().toJson(movie)
                                    navController.navigate("movieDetail/${movieJson}") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onFavoriteClick = {
                                    if (isLoggedIn.not()) {
                                        showLoginWarningDialog = true
                                        false
                                    } else {
                                        if (favList?.any { it.id == movie.id } == true) {
                                            if (currentUserId != null) {
                                                favoritesViewModel.removeFavoriteMovie(
                                                    currentUserId,
                                                    it
                                                )
                                            }
                                        } else {
                                            if (currentUserId != null) {
                                                favoritesViewModel.addFavoriteMovie(
                                                    currentUserId,
                                                    it
                                                )
                                            }
                                        }
                                        true
                                    }
                                },
                                isFavorite = favList?.any { it.id == movie.id } ?: false
                            )
                        }
                    }
                }

                is MoviesUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${(moviesState as MoviesUiState.Error).message}")
                    }
                }

                is MoviesUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.txt_no_movies))
                    }
                }
            }
            if (isSortBottomSheetVisible) {
                SortModalBottomSheet(
                    sortOption = sortOption,
                    onDismissRequest = {
                        isSortBottomSheetVisible = false
                    }, onSortOptionChange = { sortOption ->
                        homeViewModel.updateSortOption(sortOption)
                        isSortBottomSheetVisible = false
                    })
            }

        }
    }
}

// Modal Bottom Sheet for sorting
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortModalBottomSheet(
    onDismissRequest: () -> Unit,
    onSortOptionChange: (sortOption: SortOptions) -> Unit,
    sortOption: SortOptions
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.txt_sort),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SortOptions.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSortOptionChange(option)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = sortOption == option,
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onPrimary),
                        onClick = {
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option.displayName)
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    initialCount: Int = 0,
    onClick: () -> Unit,
    onFavoriteClick: (Movie) -> Boolean,
    isFavorite: Boolean = false
) {
    var isFavorite by remember { mutableStateOf(isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(10.dp)
            .clickable { onClick() }
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Movie image with overlay
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = "${Constants.IMAGE_BASE_URL}${movie.image}",
                contentDescription = "${movie.name} image",
                contentScale = ContentScale.Crop
            )

            // Top-right corner favorite button
            IconButton(
                onClick = {
                    val canToggleFavorite =
                        onFavoriteClick(movie) // User auth check for adding favorites
                    if (canToggleFavorite) {
                        isFavorite = !isFavorite
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite button",
                    tint = if (isFavorite) Color.Red else Color.White,
                )
            }

            // IMDb rating badge on the top-left corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        color = Color(0xFFf5c518),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "IMDb ${movie.rating}",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Overlay for price and actions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.name,
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${movie.price}₺",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

