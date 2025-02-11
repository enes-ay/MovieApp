package com.enesay.movieapp.data.datasource

import android.util.Log
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieActionResponse
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.service.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.EOFException

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

    suspend fun addMovieToCart(
        movie_name: String,
        movie_image: String,
        movie_price: Int,
        movie_category: String,
        movie_rating: Double,
        movie_year: Int,
        movie_director: String,
        movie_description: String,
        count: Int
    ): MovieActionResponse = withContext(
        Dispatchers.IO
    ) {
        val response = movieService.insertMovie(
            movie_name,
            movie_image,
            movie_price,
            movie_category,
            movie_rating,
            movie_year,
            movie_director,
            movie_description,
            order_amount = count,
            "enes_ay"
        )
        if (response.isSuccessful) {
            return@withContext response.body() ?: MovieActionResponse(0, "")
        } else {
            return@withContext MovieActionResponse(0, "")
        }
    }

    suspend fun getCartMovies(): List<MovieCart> = withContext(Dispatchers.IO) {
        try {
            val response = movieService.getCartMovies()
            if (response.isSuccessful) {
                return@withContext response.body()?.movie_cart ?: listOf()
            } else {
                return@withContext listOf()
            }
        } catch (e: EOFException) {
            return@withContext listOf()
        } catch (e: Exception) {
            return@withContext listOf()
        }
    }

    suspend fun deleteMovieFromCart(
        cartId: Int,
        userName: String = "enes_ay"
    ): MovieActionResponse = withContext(Dispatchers.IO) {
        try {
            val response = movieService.deleteMovie(cartId, userName)
            if (response.isSuccessful) {
                return@withContext response.body() ?: MovieActionResponse(0, "")
            } else {
                return@withContext MovieActionResponse(0, "")
            }
        } catch (e: EOFException) {
            return@withContext MovieActionResponse(0, "eof exception")
        } catch (e: Exception) {
            return@withContext MovieActionResponse(0, "")
        }

    }
}