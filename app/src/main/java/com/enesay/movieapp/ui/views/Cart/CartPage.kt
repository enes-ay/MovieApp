package com.enesay.movieapp.ui.views.Cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.shadow
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
import com.enesay.movieapp.utils.Constants.IMAGE_BASE_URL
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(navController: NavHostController) {

    val cartViewmodel: CartViewModel = hiltViewModel()
    val cartItems by cartViewmodel.cartItems.observeAsState(initial = listOf())
    val groupedmovies = cartItems.groupBy { it.name }
        .mapValues { entry -> entry.value.sumOf { it.orderAmount } }

    val totalPrice = cartItems.sumOf { it.price * it.orderAmount }

    LaunchedEffect(true) {
        cartViewmodel.getCartMovies() // current user will be added
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

        if (cartItems.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No movie found")
            }
        } else {
          Column(modifier = Modifier.fillMaxSize()) {
              LazyColumn(
                  modifier = Modifier
                      .fillMaxSize()
                      .weight(3f)
                      .padding(paddingValues)
              ) {
                  items(groupedmovies.keys.toList()) { movieName ->
                      val orderAmount = groupedmovies[movieName] ?: 0
                      val movie = cartItems.first { it.name == movieName }
                      Log.d("cart page", "movie: ${movie.orderAmount}")

                      CartItem(
                          movie = movie.copy(orderAmount = orderAmount),
                          onClick = {
                              var movieJson = Gson().toJson(movie)
                              navController.navigate("movieDetail/$movieJson") {
                                  popUpTo("cart")
                              }
                          },
                          onDelete = {
                              cartViewmodel.removeFromCart(movie.cartId)
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
                      onClick = { /* Confirm Cart action */ },
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
                      style = MaterialTheme.typography.h5,
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
    var count by remember { mutableStateOf(movie.orderAmount) }
    Log.d("cart item", "count: $count")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp) // Add padding around the card
            .clickable { onClick() }
            .shadow(8.dp, RoundedCornerShape(16.dp)), // Add shadow and round corners
        shape = RoundedCornerShape(16.dp), // Rounded corners
        // elevation = 8.dp, // Elevation for the card shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(120.dp)
                .padding(vertical = 4.dp, horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f)
            ) {

                GlideImage(
                    modifier = Modifier
                        .size(170.dp)
                        .weight(1f),
                    imageModel = "$IMAGE_BASE_URL${movie.image}",
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = movie.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${movie.price * movie.orderAmount}₺",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
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
                            IconButton(
                                modifier = Modifier.size(36.dp), // Set fixed size for the button
                                onClick = {
                                    count--
                                    onDelete()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "icon delete"
                                )
                            }
                        } else if (count > 0) {
                            IconButton(
                                modifier = Modifier.size(36.dp), // Set fixed size for the button
                                onClick = {
                                    count--
                                    onDelete()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_remove_circle),
                                    contentDescription = "icon remove"
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(36.dp)) // Reserve space when "-" button is not visible
                        }

                        if (count > 0) {
                            Text(
                                text = "$count",
                                fontSize = 22.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        IconButton(
                            modifier = Modifier.size(36.dp),
                            onClick = {
                                count++
                                onAddToCart()
                            }
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Increase Button")
                        }
                    }
                }
            }
        }
    }


}