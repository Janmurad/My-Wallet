package com.my.wallet.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.PreferencesDataStore
import com.my.wallet.model.Currency
import com.my.wallet.utils.LocaleUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesDataStore.language.collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            preferencesDataStore.setLanguage(languageCode)
            LocaleUtils.setLocale(context, languageCode)
        }
    }

    fun updateTheme(theme: Theme) {
        _uiState.update { it.copy(theme = theme) }
        // TODO: Сохранить в DataStore
    }

    fun updateDefaultCurrency(currency: Currency) {
        _uiState.update { it.copy(defaultCurrency = currency) }
        // TODO: Сохранить в DataStore
    }

    data class SettingsUiState(
        val theme: Theme = Theme.SYSTEM,
        val selectedLanguage: String = "ru",
        val defaultCurrency: Currency = Currency.RUB,
        val availableLanguages: List<Language> = listOf(
            Language("ru", "Русский"),
            Language("en", "English"),
            Language("tk", "Türkmen")
        )
    )

    data class Language(
        val code: String,
        val name: String
    )

    enum class Theme {
        SYSTEM, LIGHT, DARK
    }
} 