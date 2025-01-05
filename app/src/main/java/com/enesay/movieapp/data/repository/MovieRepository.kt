package com.enesay.movieapp.data.repository

import com.enesay.movieapp.data.datasource.MovieDataSource
import com.enesay.movieapp.data.model.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(val movieDataSource: MovieDataSource) {

    suspend fun getAllMovies() = movieDataSource.getAllMovies()
    suspend fun addMovieToCart(movie: Movie) = movieDataSource.addMovieToCart(movie)
    suspend fun getCartMovies() = movieDataSource.getCartMovies()
}