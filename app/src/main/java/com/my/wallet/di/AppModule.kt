package com.my.wallet.di

import android.content.Context
import com.my.wallet.data.WalletDatabase
import com.my.wallet.data.repository.WalletRepository

object AppModule {
    private var repository: WalletRepository? = null

    fun provideRepository(context: Context): WalletRepository {
        return repository ?: synchronized(this) {
            repository ?: WalletRepository(
                WalletDatabase.getDatabase(context)
            ).also { repository = it }
        }
    }
} 