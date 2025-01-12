package com.enesay.movieapp.ui.views.Cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.navigation.NavHostController
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.ui.theme.PrimaryBlack
import com.enesay.movieapp.ui.theme.PrimaryWhite
import com.enesay.movieapp.utils.Constants.IMAGE_BASE_URL
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(navController: NavHostController) {

//    val groupedmovies = cartItems.groupBy { it.name }
//        .mapValues { entry -> entry.value.sumOf { it.orderAmount } }

    val cartViewmodel: CartViewModel = hiltViewModel()
    val cartItems by cartViewmodel.cartItems.observeAsState(initial = listOf())
    val cartLoadingState = cartViewmodel.moviesState.value
    //val cartItems by cartViewmodel.cartItems.observeAsState(initial = listOf())

    val groupedMovies by remember {
        derivedStateOf {
            cartItems.groupBy { it.name }.mapValues { entry ->
                mapOf(
                    "amount" to entry.value.sumOf { it.orderAmount },
                    "ids" to entry.value.map { it.cartId }
                )
            }
        }
    }

    val totalPrice = cartItems.sumOf { it.price * it.orderAmount }

    LaunchedEffect(true) {
        Log.d("cart page", "LaunchedEffect")
        cartViewmodel.getCartMovies()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.txt_cart),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->

        if (cartLoadingState is CartUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            }
        } else if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.txt_no_movies),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp
                )
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                        .padding(paddingValues)
                ) {
                    items(
                        groupedMovies.keys.toList(),
                        key = { movieName -> movieName.hashCode() }) { movieName ->
                        val movieInfo = groupedMovies[movieName]
                        val orderAmount = movieInfo?.get("amount") as? Int ?: 0
                        val cartIds = movieInfo?.get("ids") as? List<Int> ?: emptyList()


                        val movie = cartItems.first { it.name == movieName }

                        CartItem(
                            movie = movie.copy(orderAmount = orderAmount, cartId = cartIds.first()),
                            onClick = {
                                val movieJson = Gson().toJson(movie)
                                navController.navigate("movieDetail/$movieJson") {
                                    popUpTo("cart")
                                }
                            },
                            onDelete = {
                                Log.d("cart id", "onDelete movie: ${movie.cartId}")
                                cartViewmodel.removeFromCart(cartIds.first())
                            },
                            onAddToCart = {
                                cartViewmodel.addToCart(
                                    movie.name,
                                    movie.image,
                                    movie.price,
                                    movie.category,
                                    movie.rating,
                                    movie.year,
                                    movie.director,
                                    movie.description,
                                    amount = 1
                                )
                            }
                        )
                    }
                    // Cart Confirm

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(1f)
                        .padding(16.dp)
                        .padding(bottom = 56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Confirm Cart Button
                    Button(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 200.dp)
                            .padding(end = 10.dp),
                        onClick = { /* Confirm Cart action */
                            navController.navigate("payment")
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(
                            text = stringResource(id = R.string.txt_confirmCart),
                            color = Color.White,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.txt_total) + " ${totalPrice}₺",
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

        }
    }
}


@Composable
fun CartItem(
    movie: MovieCart,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onAddToCart: () -> Unit,
) {
    var count by remember { mutableIntStateOf(movie.orderAmount) }
    val gradientColors = listOf(
        Color(0xFF0D0D0D), // Dark Black
        Color(0xFF262626)  // Light Black
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(gradientColors))
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(12.dp)
            ) {
                // Movie Thumbnail
                GlideImage(
                    modifier = Modifier
                        .size(100.dp, 120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    imageModel = "$IMAGE_BASE_URL${movie.image}",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(22.dp))

                // Movie Details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                    .weight(1.3f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = movie.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "IMDb: ${movie.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Yellow.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${movie.price * count}₺",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // Quantity Controls
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.5f),
                    horizontalAlignment = Alignment.End
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (count == 1) {
                                // Movie remove button
                                IconButton(
                                    modifier = Modifier.size(36.dp), // Set fixed size for the button
                                    onClick = {
                                        count--
                                        onDelete()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "icon delete",
                                        tint = PrimaryWhite
                                    )
                                }
                            } else if (count > 0) {
                                // Movie remove button
                                IconButton(
                                    modifier = Modifier.size(36.dp), // Set fixed size for the button
                                    onClick = {
                                        count--
                                        onDelete()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_remove_circle),
                                        contentDescription = "icon remove",
                                        tint = PrimaryWhite
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp)) // Reserve space when "-" button is not visible
                            }

                            if (count > 0) {
                                Text(
                                    text = "$count",
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = PrimaryWhite
                                )
                            }

                            IconButton(
                                modifier = Modifier.size(36.dp),
                                onClick = {
                                    count++
                                    onAddToCart()
                                }
                            ) {
                                Icon(
                                    Icons.Default.AddCircle,
                                    contentDescription = "Increase Button",
                                    tint = PrimaryWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}