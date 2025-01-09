package com.enesay.movieapp.ui.views.Home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.repository.FirebaseRepository
import com.enesay.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val favoritesRepository: FirebaseRepository
) : ViewModel() {

    private val _moviesState = mutableStateOf<MoviesUiState>(MoviesUiState.Loading)
    val moviesState: State<MoviesUiState> = _moviesState

    private val _sortOption = MutableStateFlow(SortOptions.NAME)
    val sortOption: StateFlow<SortOptions> get() = _sortOption

    init {
        getMovies()
        // todo migrate to firebase auth asap
        favoritesRepository.getFavoriteMovies("4p9Y5d9wQwQYMWtnZG5l")
    }

    fun getMovies() {
        viewModelScope.launch {
            _moviesState.value = MoviesUiState.Loading
            delay(700) // For simulate real network delay
            try {
                val movieList = repository.getAllMovies()
                if (movieList.isEmpty()) {
                    _moviesState.value = MoviesUiState.Empty
                } else {
                    val sortedMovies = sortMovies(movieList, _sortOption.value)
                    _moviesState.value = MoviesUiState.Success(sortedMovies)
                }
            } catch (e: Exception) {
                _moviesState.value = MoviesUiState.Error("Failed to load movies: ${e.message}")
            }
        }
    }

    fun updateSortOption(option: SortOptions) {
        _sortOption.value = option
        if (_moviesState.value is MoviesUiState.Success) {
            val sortedMovies = sortMovies(
                (_moviesState.value as MoviesUiState.Success).movies,
                option
            )
            _moviesState.value = MoviesUiState.Success(sortedMovies)
        }
    }

    // Sort movies based on the selected option
    private fun sortMovies(movies: List<Movie>, sortOption: SortOptions): List<Movie> {
        return when (sortOption) {
            SortOptions.NAME -> movies.sortedBy { it.name }
            SortOptions.PRICE_LOW_TO_HIGH -> movies.sortedBy { it.price }
            SortOptions.PRICE_HIGH_TO_LOW -> movies.sortedByDescending { it.price }
            SortOptions.RATING -> movies.sortedByDescending { it.rating }
            SortOptions.YEAR -> movies.sortedByDescending { it.year }
        }
    }
}
