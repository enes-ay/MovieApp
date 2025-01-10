package com.enesay.movieapp.data.repository

import androidx.lifecycle.MutableLiveData
import com.enesay.movieapp.data.datasource.FirebaseDataSource
import com.enesay.movieapp.data.model.Movie
import javax.inject.Inject

class FirebaseRepository @Inject constructor(val dataSource: FirebaseDataSource) {


    suspend fun addFavoriteMovie(userId: String, movie: Movie) : Result<Unit> {
        return dataSource.addFavoriteMovie(userId, movie)
    }

    suspend fun removeFavoriteMovie(userId: String, movie: Movie): Result<Unit> {
        return dataSource.removeFavoriteMovie(userId, movie)
    }

    suspend fun getFavoriteMovies(userId: String): MutableLiveData<List<Movie>?> = dataSource.getFavoriteMovies(userId)
}