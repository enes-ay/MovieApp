package com.enesay.movieapp.ui.views.Favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.repository.AuthRepository
import com.enesay.movieapp.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(val repository: FirebaseRepository,
                                             val authRepository: AuthRepository
) : ViewModel() {
    private val _favorites = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favorites
    val currentUser = authRepository.getCurrentUserId()

    init {
        if (currentUser != null) {
            getFavoriteMovies(currentUser)
        }
    }

    fun addFavoriteMovie(userId: String, movie: Movie) {
        viewModelScope.launch {
            repository.addFavoriteMovie(userId, movie)
        }
        getFavoriteMovies(userId)
    }

    fun removeFavoriteMovie(userId: String, movie: Movie) {
        viewModelScope.launch {
            val result = repository.removeFavoriteMovie(userId, movie)
            result.onSuccess {
                // Successfully removed the movie from favorites
                Log.d("favorites", "Remove favorite success")

                // Optionally, refresh the favorites list
                getFavoriteMovies(userId)
            }.onFailure { exception ->
                // Handle failure
                Log.e("favorites", "Remove favorite error: ${exception.message}")
            }
        }
    }

    fun getFavoriteMovies(userId: String) {
        viewModelScope.launch {
            _favorites.value = repository.getFavoriteMovies(userId).value
        }
        Log.d("favorites", "get fav vm ${_favorites.value}")
    }
}