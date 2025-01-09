package com.enesay.movieapp.ui.views.Home

import com.enesay.movieapp.data.model.Movie

sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
    object Empty : MoviesUiState()
}
