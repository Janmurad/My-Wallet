package com.my.wallet.di

import android.content.Context
import com.my.wallet.data.WalletDatabase
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.data.PreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WalletDatabase {
        return WalletDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRepository(database: WalletDatabase): WalletRepository {
        return WalletRepository(database)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): PreferencesDataStore {
        return PreferencesDataStore(context)
    }
} 