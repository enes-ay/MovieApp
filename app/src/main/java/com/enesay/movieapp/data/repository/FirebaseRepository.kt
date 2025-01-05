package com.enesay.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enesay.movieapp.data.datasource.FirebaseDataSource
import com.enesay.movieapp.data.model.Movie
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class FirebaseRepository @Inject constructor(val dataSource: FirebaseDataSource) {


    fun addFavoriteMovie(userId: String, movie: Movie): Task<Void> {
        return dataSource.addFavoriteMovie(userId, movie)
    }

    fun removeFavoriteMovie(userId: String, movieId: Int): Task<Void> {
        return dataSource.removeFavoriteMovie(userId, movieId)
    }

    fun getFavoriteMovies(userId: String): MutableLiveData<List<Movie>?> = dataSource.getFavoriteMovies(userId)
}