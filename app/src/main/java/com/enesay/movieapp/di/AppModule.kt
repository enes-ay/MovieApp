package com.enesay.movieapp.di

import com.enesay.movieapp.data.datasource.MovieDataSource
import com.enesay.movieapp.data.repository.AuthRepository
import com.enesay.movieapp.data.repository.MovieRepository
import com.enesay.movieapp.data.repository_refactor.CartRepositoryImpl
import com.enesay.movieapp.domain.repository.CartRepository
import com.enesay.movieapp.service.ApiUtils
import com.enesay.movieapp.service.GoogleAccountService
import com.enesay.movieapp.service.MovieService
import com.enesay.movieapp.ui.views.Cart.CartViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieService(): MovieService {
        return ApiUtils.getMovieService()
    }

    @Singleton
    @Provides
    fun provideMovieDataSource(movieService: MovieService): MovieDataSource {
        return MovieDataSource(movieService)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(movieDataSource: MovieDataSource): MovieRepository {
        return MovieRepository(movieDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        collectionReference: CollectionReference,
        accountService: GoogleAccountService
    ): AuthRepository {
        return AuthRepository(firebaseAuth, collectionReference, accountService)
    }

    @Provides
    @Singleton
    fun ProvideCartRepositor(service: MovieService) : CartRepository {
        return CartRepositoryImpl(service)
    }
}