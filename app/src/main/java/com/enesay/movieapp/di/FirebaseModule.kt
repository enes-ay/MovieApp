package com.enesay.movieapp.di

import com.enesay.movieapp.data.datasource.FirebaseDataSource
import com.enesay.movieapp.data.repository.FirebaseRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("users")
    }

    @Provides
    @Singleton
    fun provideFirebaseDataSource(usersCollection: CollectionReference): FirebaseDataSource {
        return FirebaseDataSource(usersCollection)
    }


    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseDataSource: FirebaseDataSource): FirebaseRepository {
        return FirebaseRepository(firebaseDataSource)
    }


}