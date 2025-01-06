package com.enesay.movieapp.data.repository

import android.util.Log
import com.enesay.movieapp.common.Resource
import com.enesay.movieapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val colectionReference: CollectionReference
) {
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        surname: String
    ): Resource<String> {
        return try {
            // Firebase auth user creation
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid.orEmpty()

            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signOut(): Resource<String> {
        return try {
            firebaseAuth.signOut()
            Resource.Success("User signed out successfully")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getUserProfile(userId: String): Resource<User> {
        return try {
            val snapshot =
                colectionReference.document(userId).collection("Users").document(userId).get()
                    .await()
            if (snapshot.exists()) {
                val userProfile = snapshot.toObject(User::class.java)
                if (userProfile != null) {
                    Resource.Success(userProfile)
                } else {
                    Resource.Error(Exception("User profile data is null"))
                }
            } else {
                Resource.Error(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    // Get the user auth value
    fun isUserLoggedIn(): Boolean = try {
        firebaseAuth.currentUser != null
    } catch (e: Exception) {
        Log.e("AuthRepository", "Error checking user login status: ${e.message}")
        false
    }

    // Get the current user's ID
    fun getCurrentUserId(): String? = try {
        firebaseAuth.currentUser?.uid
    } catch (e: Exception) {
        Log.e("AuthRepository", "Error getting current user ID: ${e.message}")
        null
    }
}