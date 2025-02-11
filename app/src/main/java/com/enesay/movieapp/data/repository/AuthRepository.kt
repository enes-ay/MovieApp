package com.enesay.movieapp.data.repository

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.enesay.movieapp.common.Resource
import com.enesay.movieapp.data.model.User
import com.enesay.movieapp.service.GoogleAccountService
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val colectionReference: CollectionReference,
    private val accountService: GoogleAccountService
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
            if (userId.isNotEmpty()) {
                initializeFirestoreData(userId, name, surname, email)
            }

            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    private suspend fun initializeFirestoreData(
        userId: String,
        name: String,
        surname: String,
        email: String
    ) {
        val userDocument = mapOf(
            "name" to name,
            "surname" to surname,
            "email" to email,
            "favorites" to emptyList<Map<String, Any>>()
        )

        colectionReference
            .document(userId)
            .set(userDocument)
            .await()
    }

    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun signOut(): Resource<String> {
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
                colectionReference.document(userId).get()
                    .await()
            if (snapshot.exists()) {
                val userProfile = snapshot.toObject(User::class.java)
                if (userProfile != null) {
                    Log.d("user", "$userProfile")

                    Resource.Success(userProfile)
                } else {
                    Log.d("user", "User profile data is null")
                    Resource.Error(Exception("User profile data is null"))
                }
            } else {
                Log.d("user", "User profile not found")
                Resource.Error(Exception("User profile not found"))
            }
        } catch (e: Exception) {
            Log.e("user", "catch profile: ${e.message}")
            Resource.Error(e)
        }
    }

    // Get the user auth value
    fun isUserLoggedIn(): Boolean = try {
        Log.d("auth", "${firebaseAuth.currentUser}")
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

    suspend fun signInWithGoogle(credential: Credential) : Resource<String> {
       return  try {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                delay(1000)
                firebaseAuth.currentUser?.uid?.let { initializeFirestoreData(it, googleIdTokenCredential.displayName ?: "unknown", googleIdTokenCredential.familyName ?: "unknown", googleIdTokenCredential.id) }
                Log.d("credential", "${googleIdTokenCredential} ${googleIdTokenCredential.id} ${googleIdTokenCredential.displayName}")
                //initializeFirestoreData(googleIdTokenCredential.id, googleIdTokenCredential.displayName ?: "unknown", googleIdTokenCredential.familyName ?: "unknown", googleIdTokenCredential.idToken)
                Resource.Success(googleIdTokenCredential.idToken)
            } else {
                Resource.Error(Exception("Invalid credential type"))
            }
        } catch (e: Exception) {
            Log.e("credential", e.message.toString())
            Resource.Error(e)
        }
    }
}