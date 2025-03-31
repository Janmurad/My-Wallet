package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Account
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllAccounts()
                .map { accounts -> AccountsUiState(accounts = accounts) }
                .catch { e -> AccountsUiState(error = e.message) }
                .collect { state -> _uiState.value = state }
        }
    }

    fun addAccount(account: Account) {
        viewModelScope.launch {
            try {
                repository.addAccount(account)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            try {
                repository.deleteAccount(account)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    data class AccountsUiState(
        val accounts: List<Account> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    class Factory(private val repository: WalletRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AccountsViewModel(repository) as T
        }
    }
} 