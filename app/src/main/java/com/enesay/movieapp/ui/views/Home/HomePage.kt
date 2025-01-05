package com.enesay.movieapp.ui.views.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.enesay.movieapp.utils.Constants
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomePage(navController: NavController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val movies by homeViewModel.movies.observeAsState()

    LaunchedEffect(true) {
        homeViewModel.getMovies()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                columns = GridCells.Fixed(2)
            ) {
                if (movies != null){
                    items(movies!!){
                        MovieCard(movie = it, onAddToCart = {}, onRemoveFromCart = {}, onClick = {
                            val movieJson = Gson().toJson(it)
                            navController.navigate("movieDetail/${movieJson}"

                            )
                        }, onFavoriteClick = {})
                    }
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
            ){
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
                    modifier = Modifier.fillMaxWidth().padding(3.dp),
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
