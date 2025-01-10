package com.enesay.movieapp.ui.views.Cart

import com.enesay.movieapp.data.model.MovieCart
import com.enesay.movieapp.ui.views.Home.MoviesUiState

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val movies: List<MovieCart>) : CartUiState()
}
