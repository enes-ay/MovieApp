package com.enesay.movieapp.ui.views.Cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}