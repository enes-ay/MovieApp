package com.enesay.movieapp.data.datasource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.enesay.movieapp.data.model.Movie
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(val usersCollection: CollectionReference) {
    private val _favorites = MutableLiveData<List<Movie>?>()
    val favoriteMovies: LiveData<List<Movie>?> = _favorites

    suspend fun addFavoriteMovie(userId: String, movie: Movie): Result<Unit> {
        return try {
            usersCollection.document(userId)
                .update("favorites", FieldValue.arrayUnion(movie))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("favorites", "add fav ds error ${e.message}")
            Result.failure(e)
        }
    }

    // Remove fav movie
    suspend fun removeFavoriteMovie(userId: String, movie: Movie): Result<Unit> {
        return try {
            usersCollection.document(userId)
                .update("favorites", FieldValue.arrayRemove(movie))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("favorites", "Remove favorite error: ${e.message}")
            Result.failure(e)
        }
    }

    fun getFavoriteMovies(userId: String): MutableLiveData<List<Movie>?> {
        usersCollection.document(userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.d("favorites", "Real-time listener error: ${exception.message}")
                    _favorites.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val favoritesList = snapshot.get("favorites") as? List<HashMap<String, Any>> ?: emptyList()
                    val favorites = favoritesList.mapNotNull { map ->
                        try {
                            // HashMap to Movie object
                            Movie(
                                id = (map["id"] as? Long)?.toInt() ?: 0,
                                name = map["name"] as? String ?: "",
                                description = map["description"] as? String ?: "",
                                category = map["category"] as? String ?: "",
                                director = map["director"] as? String ?: "",
                                image = map["image"] as? String ?: "",
                                price = (map["price"] as? Long)?.toInt() ?: 0,
                                rating = (map["rating"] as? Double)?.toDouble() ?: 0.0,
                                year = (map["year"] as? Long)?.toInt() ?: 0,
                            )
                        } catch (e: Exception) {
                            Log.d("favorites", "casting error ${e.message}")
                            null // Hatalı bir veri varsa, listeye eklenmez
                        }
                    }
                    Log.d("favorites", "Updated list: $favorites")
                    _favorites.postValue(favorites)
                } else {
                    _favorites.postValue(emptyList())
                }
            }
        return _favorites
    }
}