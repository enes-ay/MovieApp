package com.enesay.movieapp.ui.views.Cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.data.model.MovieCartResponse
import com.enesay.movieapp.data.repository.MovieRepository
import com.enesay.movieapp.ui.views.BottomNavItem
import com.enesay.movieapp.ui.views.Home.MoviesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val movieRepository: MovieRepository) : ViewModel()  {
    private val _cartItems = MutableLiveData<List<MovieCart>>()
    val cartItems: LiveData<List<MovieCart>> = _cartItems

    private val _cartMovieState = mutableStateOf<CartUiState>(CartUiState.Loading)
    val moviesState: State<CartUiState> = _cartMovieState

    init {
        getCartMovies()
    }

    fun getCartMovies(){
        viewModelScope.launch {
            _cartMovieState.value = CartUiState.Loading
            delay(500)
            _cartItems.value = movieRepository.getCartMovies()
            Log.d("cart viewmodel", "getCartMovies: ${_cartItems.value!!.size}")
            delay(500) // UI'nın güncellenmesini beklemek için
            _cartMovieState.value = CartUiState.Success(_cartItems.value ?: emptyList())
        }
    }

    fun addToCart(movie_name: String, movie_image: String, movie_price: Int, movie_category: String, movie_rating: Double, movie_year: Int, movie_director: String, movie_description: String, amount: Int) {
       viewModelScope.launch {
           movieRepository.addMovieToCart(movie_name, movie_image, movie_price, movie_category, movie_rating, movie_year, movie_director, movie_description, amount)
           delay(1000)
       }
        getCartMovies()
    }

    fun removeFromCart(cartId: Int) {
        viewModelScope.launch {
            movieRepository.deleteMovieFromCart(cartId)
            if (_cartItems.value.isNullOrEmpty()) {
                _cartItems.value = emptyList()
            } else {
                _cartItems.value = _cartItems.value?.filter { it.cartId != cartId }
            }
        }
    }
}