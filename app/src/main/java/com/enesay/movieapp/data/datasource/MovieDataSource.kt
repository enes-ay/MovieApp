package com.enesay.movieapp.data.datasource

import android.util.Log
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieActionResponse
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.service.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDataSource(var movieService: MovieService) {

    suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        Log.d("home page", "getAllMovies: repo")
        try {
            val response = movieService.getMovies()
            Log.d("home page", response.message())
            if (response.isSuccessful) {
                Log.d("home page success", response.body().toString())
                return@withContext response.body()?.movies ?: listOf()
            } else {
                return@withContext listOf()

            }
        } catch (e: Exception) {
            Log.e("home page catch", e.toString())
            return@withContext listOf()
        }
    }

    suspend fun addMovieToCart(movie: Movie): MovieActionResponse = withContext(
        Dispatchers.IO
    ) {
        val response = movieService.insertMovie(
            movie.name,
            movie.image,
            movie.price,
            movie.category,
            movie.rating,
            movie.year,
            movie.director,
            movie.description,
            1,
            "enes_ay"
        )
        if (response.isSuccessful) {
            return@withContext response.body() ?: MovieActionResponse(0, "")
        } else {
            return@withContext MovieActionResponse(0, "")
        }
    }

    suspend fun getCartMovies(): List<MovieCart> = withContext(Dispatchers.IO) {
        val response = movieService.getCartMovies()
        if (response.isSuccessful) {
            return@withContext response.body()?.movie_cart ?: listOf()
        } else {
            return@withContext listOf()
        }
    }
}