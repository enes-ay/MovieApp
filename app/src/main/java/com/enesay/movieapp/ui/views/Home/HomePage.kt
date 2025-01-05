package com.enesay.movieapp.ui.views.Home

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.utils.Constants
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val movies by homeViewModel.movies.observeAsState()
    var searchQuery by remember { mutableStateOf("") } // Arama terimini saklar
    var filteredMovieList = movies?.filter { movie ->
        movie.name.contains(searchQuery, ignoreCase = true)
    } ?: listOf() // Arama terimine göre filtreleme
    val focusManager = LocalFocusManager.current  // search focus kontrol
    var isFocused by remember { mutableStateOf(false) }
    val sortOption by homeViewModel.sortOption.collectAsState()
    var isSortBottomSheetVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        homeViewModel.getMovies()
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            // Top App Bar with Search and Filter Row
            Column {
                // Top App Bar
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
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
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused // Tracks focus state
                        }
                )

                // Filter Row
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = "Sort Icon",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(id = R.string.txt_sort),
                            color = Color.White
                        )
                    }
                    // Filter Button
                    Button(
                        onClick = {
                            // Handle filter logic
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Filter Icon",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(id = R.string.txt_filter),
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Scaffold'dan gelen padding
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    bottom = WindowInsets.systemBars.asPaddingValues()
                        .calculateBottomPadding() + 56.dp // Bottom nav yüksekliğini ekle
                )
            ) {
                if (movies != null) {
                    items(filteredMovieList) { movie ->
                        MovieCard(movie = movie, onAddToCart = {

                        }, onRemoveFromCart = {
                        }, onClick = {
                            val movieJson = Gson().toJson(movie)
                            navController.navigate(
                                "movieDetail/${movieJson}"

                            )
                        }, onFavoriteClick = {})
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
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.txt_sort),
                style = MaterialTheme.typography.labelMedium,
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
    onAddToCart: () -> Unit,
    onRemoveFromCart: (Movie) -> Unit,
    onClick: () -> Unit,
    onFavoriteClick: (Movie) -> Unit
) {
    var count by remember { mutableStateOf(initialCount) }
    var isFavorite by remember { mutableStateOf(false) }

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .padding(10.dp)
            ) {
                // Favorite button at the top-right corner
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick(movie)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite button",
                        tint = if (isFavorite) Color.Red else Color.White,
                    )
                }
            }
            // Movie image with overlay
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = "${Constants.IMAGE_BASE_URL}${movie.image}",
                contentDescription = "${movie.name} image",
                contentScale = ContentScale.Crop
            )

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
