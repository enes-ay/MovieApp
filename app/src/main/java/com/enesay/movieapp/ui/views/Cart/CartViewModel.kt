package com.enesay.movieapp.ui.views.Cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.model.MovieCartResponse
import com.enesay.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val movieRepository: MovieRepository) : ViewModel()  {
    private val _cartItems = MutableLiveData<List<MovieCart>>()
    val cartItems: LiveData<List<MovieCart>> = _cartItems

    init {
        getCartMovies()
    }

    fun getCartMovies(){
        CoroutineScope(Dispatchers.Main).launch {
            _cartItems.value = movieRepository.getCartMovies()
        }
    }

    fun addToCart(movie_name: String, movie_image: String, movie_price: Int, movie_category: String, movie_rating: Double, movie_year: Int, movie_director: String, movie_description: String, amount: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            movieRepository.addMovieToCart(movie_name, movie_image, movie_price, movie_category, movie_rating, movie_year, movie_director, movie_description, amount)
            getCartMovies()
        }
    }

    fun removeFromCart(cartId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            movieRepository.deleteMovieFromCart(cartId)
            getCartMovies()
        }
    }
}