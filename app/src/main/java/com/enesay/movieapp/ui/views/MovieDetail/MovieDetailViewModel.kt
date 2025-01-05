package com.enesay.movieapp.ui.views.MovieDetail

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
class MovieDetailViewModel @Inject constructor(var movieRepository: MovieRepository) : ViewModel() {


    val result = MutableLiveData<String>()

    fun addToCart(movie: Movie) {
        CoroutineScope(Dispatchers.Main).launch {
            if (movieRepository.addMovieToCart(movie).success == 1) {
                result.value = "success"
            } else {
                result.value = "error"
            }
        }
    }
}