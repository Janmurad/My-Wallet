package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Account
import com.my.wallet.model.Transaction
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Комбинируем потоки счетов и транзакций
            combine(
                repository.getAllAccounts(),
                repository.getAllTransactions()
            ) { accounts, transactions ->
                HomeUiState(
                    accounts = accounts,
                    recentTransactions = transactions.take(5),
                    totalBalance = accounts.sumOf { it.balance }
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    data class HomeUiState(
        val accounts: List<Account> = emptyList(),
        val recentTransactions: List<Transaction> = emptyList(),
        val totalBalance: java.math.BigDecimal = java.math.BigDecimal.ZERO,
        val isLoading: Boolean = false,
        val error: String? = null
    )
} 