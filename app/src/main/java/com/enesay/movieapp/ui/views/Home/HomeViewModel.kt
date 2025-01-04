package com.enesay.movieapp.ui.views.Home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(val repository: MovieRepository): ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        getMovies()
    }

    fun getMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            _movies.value = repository.getAllMovies()
            Log.d("homeViewModel", "getMovies: ${_movies.value}")
         }
    }
}