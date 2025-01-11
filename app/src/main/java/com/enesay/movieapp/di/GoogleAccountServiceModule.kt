package com.enesay.movieapp.di

import com.enesay.movieapp.data.repository.AccountServiceImpl
import com.enesay.movieapp.service.GoogleAccountService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): GoogleAccountService
}