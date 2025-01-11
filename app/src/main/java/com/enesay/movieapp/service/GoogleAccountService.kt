package com.enesay.movieapp.service

interface GoogleAccountService {
    suspend fun signInWithGoogle(idToken: String)
}