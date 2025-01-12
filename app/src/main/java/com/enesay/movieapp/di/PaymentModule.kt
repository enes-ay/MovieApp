package com.enesay.movieapp.di

import com.enesay.movieapp.data.repository.PaymentRepository
import com.enesay.movieapp.data.repository.PaymentRepositoryImpl
import com.google.firebase.firestore.CollectionReference
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {
    @Provides
    @Singleton
    fun providePaymentRepository(usersCollection: CollectionReference): PaymentRepository {
        return PaymentRepositoryImpl(usersCollection)
    }
}
