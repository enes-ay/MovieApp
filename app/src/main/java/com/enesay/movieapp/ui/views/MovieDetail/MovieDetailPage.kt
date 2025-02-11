package com.enesay.movieapp.ui.views.MovieDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.ui.theme.dm_serif
import com.enesay.movieapp.ui.views.Cart.CartUiState
import com.enesay.movieapp.utils.Constants.IMAGE_BASE_URL
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailPage(navController: NavController, movie: Movie) {
    var count by remember { mutableStateOf(0) }
    val totalPrice = count * movie.price
    val movieDetailViewModel: MovieDetailViewModel = hiltViewModel()

    //val cartUiState by movieDetailViewModel.cartUiState.collectAsState()

//    LaunchedEffect(cartUiState) {
//        if (cartUiState is CartUiState.Success) {
//            navController.navigate("cart") {
//                popUpTo(navController.graph.findStartDestination().id) {
//                    // saveState = true
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
//            movieDetailViewModel.resetUiState()
//        }
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Detail", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Transient background
            GlideImage(
                imageModel = "$IMAGE_BASE_URL${movie.image}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Movie title
                    Text(
                        text = movie.name,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = dm_serif
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFf5c518),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "IMDb ${movie.rating}",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Movie poster
                GlideImage(
                    imageModel = "$IMAGE_BASE_URL${movie.image}",
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Movie details
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = movie.director,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${movie.year}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${movie.category}",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
//                        Text(
//                            text = "IMDB: ${movie.rating}",
//                            color = Color.White,
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold
//                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 15.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "${movie.price}₺",
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Text(
                        text = movie.description,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add to cart button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (count == 0) {
                        Button(
                            onClick = {
                                count++
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.txt_add_to_cart),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { if (count > 0) count-- },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (count == 1) R.drawable.ic_delete else R.drawable.ic_remove_circle
                                    ),
                                    contentDescription = "Decrease",
                                    tint = Color.White
                                )
                            }
                            Text(
                                text = "$count",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            IconButton(
                                onClick = { count++ },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.AddCircle,
                                    contentDescription = "Increase",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Go to cart button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (count > 0) {
                                movieDetailViewModel.addToCart(
                                    movie.name,
                                    movie.image,
                                    movie.price,
                                    movie.category,
                                    movie.rating,
                                    movie.year,
                                    movie.director,
                                    movie.description,
                                    amount = count
                                )
                                navController.navigate("cart") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            } else {
                                navController.navigate("cart") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        //  saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        modifier = Modifier
                            .border(1.dp, Color.White, CircleShape)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.txt_go_to_cart),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "${stringResource(id = R.string.txt_total)} $totalPrice₺",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
