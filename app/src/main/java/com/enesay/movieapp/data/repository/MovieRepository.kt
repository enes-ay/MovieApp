package com.enesay.movieapp.data.repository

import com.enesay.movieapp.data.datasource.MovieDataSource
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import javax.inject.Inject

class MovieRepository @Inject constructor(val movieDataSource: MovieDataSource) {

    suspend fun getAllMovies() = movieDataSource.getAllMovies()
    suspend fun addMovieToCart(
        movie_name: String,
        movie_image: String,
        movie_price: Int,
        movie_category: String,
        movie_rating: Double,
        movie_year: Int,
        movie_director: String,
        movie_description: String,
        amount: Int
    ) = movieDataSource.addMovieToCart(movie_name, movie_image, movie_price, movie_category, movie_rating, movie_year, movie_director, movie_description, amount)

    suspend fun getCartMovies() = movieDataSource.getCartMovies()
    suspend fun deleteMovieFromCart(cartId: Int) = movieDataSource.deleteMovieFromCart(cartId)
}