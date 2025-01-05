package com.enesay.movieapp.ui.views.Home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enesay.movieapp.data.model.Movie
import com.enesay.movieapp.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _sortOption = MutableStateFlow(SortOptions.NAME)
    val sortOption: StateFlow<SortOptions> get() = _sortOption

    init {
        getMovies()
    }

    fun getMovies() {
        CoroutineScope(Dispatchers.Main).launch {
            val movieList = repository.getAllMovies()
            _movies.value = sortMovies(movieList, _sortOption.value)  // cekilen liste default degere gore sıralanır
            Log.d("homeViewModel", "getMovies: ${_movies.value}")
        }
    }

    fun updateSortOption(option: SortOptions) {
        _sortOption.value = option
        _movies.value = _movies.value?.let { sortMovies(it, option) }
    }

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