package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.my.wallet.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun updateTheme(theme: Theme) {
        _uiState.update { it.copy(theme = theme) }
        // TODO: Сохранить в DataStore
    }

    fun updateLanguage(language: Language) {
        _uiState.update { it.copy(language = language) }
        // TODO: Сохранить в DataStore
    }

    fun updateDefaultCurrency(currency: Currency) {
        _uiState.update { it.copy(defaultCurrency = currency) }
        // TODO: Сохранить в DataStore
    }

    data class SettingsUiState(
        val theme: Theme = Theme.SYSTEM,
        val language: Language = Language.SYSTEM,
        val defaultCurrency: Currency = Currency.RUB
    )

    enum class Theme {
        SYSTEM, LIGHT, DARK
    }

    enum class Language {
        SYSTEM, RU, EN
    }
} 