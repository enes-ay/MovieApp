package com.enesay.movieapp.data.repository

import com.enesay.movieapp.service.GoogleAccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(var firebaseAuth: FirebaseAuth) : GoogleAccountService {

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }
}