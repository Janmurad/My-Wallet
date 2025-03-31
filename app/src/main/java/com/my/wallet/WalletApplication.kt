package com.my.wallet

import android.app.Application
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.di.AppModule

class WalletApplication : Application() {
    lateinit var repository: WalletRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = AppModule.provideRepository(this)
    }
} 