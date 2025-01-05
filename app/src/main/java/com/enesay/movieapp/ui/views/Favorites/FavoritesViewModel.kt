package com.enesay.movieapp.ui.views.Favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(val repository: FirebaseRepository) : ViewModel() {
    private val _favorites = MutableLiveData<List<Movie>>()
    val favoriteMovies: LiveData<List<Movie>> = _favorites

    init {
      getFavoriteMovies("4p9Y5d9wQwQYMWtnZG5l")
    }

    fun addFavoriteMovie(userId: String, movie: Movie) {
        repository.addFavoriteMovie(userId, movie)
            .addOnSuccessListener {
                _favorites.value = repository.getFavoriteMovies(userId).value
                // Handle success (e.g., show a Toast)
            }
            .addOnFailureListener {
                // Handle failure (e.g., show an error message)
            }
    }

    fun removeFavoriteMovie(userId: String, movieId: Int) {
        repository.removeFavoriteMovie(userId, movieId)
            .addOnSuccessListener {
                // Handle success
                _favorites.value = repository.getFavoriteMovies(userId).value
                Log.d("favorites", "remove fav vm success")

            }
            .addOnFailureListener {
                // Handle failure
                Log.d("favorites", "remove fav vm error")

            }
    }

    fun getFavoriteMovies(userId: String) {
        _favorites.value = repository.getFavoriteMovies(userId).value
        Log.d("favorites", "get fav vm ${_favorites.value}")
    }
}