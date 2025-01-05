package com.enesay.movieapp.ui.views.MovieDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(var movieRepository: MovieRepository) : ViewModel() {


    val result = MutableLiveData<String>()

    fun addToCart(
        movie_name: String,
        movie_image: String,
        movie_price: Int,
        movie_category: String,
        movie_rating: Double,
        movie_year: Int,
        movie_director: String,
        movie_description: String,
        amount: Int
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            if (movieRepository.addMovieToCart(
                    movie_name,
                    movie_image,
                    movie_price,
                    movie_category,
                    movie_rating,
                    movie_year,
                    movie_director,
                    movie_description,
                    amount
                ).success == 1
            ) {
                result.value = "success"
            } else {
                result.value = "error"
            }
        }
    }
}