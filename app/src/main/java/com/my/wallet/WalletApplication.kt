package com.my.wallet

import android.app.Application
import androidx.lifecycle.lifecycleScope
import com.my.wallet.data.PreferencesDataStore
import com.my.wallet.utils.LocaleUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class WalletApplication : Application() {
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore
    
    override fun onCreate() {
        super.onCreate()
        
        // Установка языка при запуске приложения
        applicationScope.launch {
            preferencesDataStore.language.collect { language ->
                LocaleUtils.setLocale(this@WalletApplication, language)
            }
        }
    }
} 