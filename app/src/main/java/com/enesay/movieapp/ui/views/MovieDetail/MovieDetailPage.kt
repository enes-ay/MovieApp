package com.enesay.movieapp.ui.views.MovieDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.enesay.movieapp.R
import com.enesay.movieapp.data.model.Movie
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailPage(navController: NavController, movie: Movie) {
    var count by remember { mutableStateOf(0) }
    val totalPrice = count*movie.price
    val movieDetailViewModel : MovieDetailViewModel = hiltViewModel()


    Scaffold (modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { "Movie Detail" }) }) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column ( modifier = Modifier
                .fillMaxSize()
                .weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(movie.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)

                GlideImage(modifier = Modifier.size(450.dp, 450.dp),
                    imageModel = "http://kasimadalan.pe.hu/movies/images/${movie.image}",
                    contentScale = ContentScale.Fit,
                )
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text("${movie.price}₺", fontSize = 27.sp, fontWeight = FontWeight.Bold)
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    if (count == 0) {
                        // "Sepete Ekle" butonu
                        Button(
                            onClick = {
                                count++
                                movieDetailViewModel.addToCart(movie)
                                navController.navigate("cart")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                                .padding(top = 8.dp), // Sabit yükseklik
                            shape = RoundedCornerShape(4.dp), // Yuvarlak köşeler
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue, // Tema rengine göre arka plan
                                contentColor = Color.White // Beyaz yazı rengi
                            )
                        ) {
                            Text(text =  stringResource(id = R.string.txt_add_to_cart), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // "-" butonu, sayacı ve "+" butonu
                        IconButton(
                            modifier = Modifier
                                .size(43.dp)
                                .padding(top = 14.dp), // Sabit boyut
                            onClick = {
                                count--
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (count == 1) R.drawable.ic_delete else R.drawable.ic_remove_circle
                                ),
                                contentDescription = "icon remove",
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.error // Kaldırma butonu için hata rengi
                            )
                        }

                        // Miktar gösterimi
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 13.dp)
                                .padding(top = 14.dp),
                            text = "$count",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        // "+" butonu
                        IconButton(
                            modifier = Modifier
                                .size(43.dp)
                                .padding(top = 14.dp),
                            onClick = {
                                count++
                            }
                        ) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = "Increase Button",
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.primary // Ekleme butonu için tema rengi
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 200.dp)
                        .padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    onClick = {
                        if(count>0){
                            // perform viewmodel add to cart
                            navController.navigate("cart")
                        }
                        else{
                            navController.navigate("cart"){
                                popUpTo("cart")
                            }
                        }

                    },
                    shape = RectangleShape
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_go_to_cart),
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(id = R.string.txt_total) + "  $totalPrice₺",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

    }

}