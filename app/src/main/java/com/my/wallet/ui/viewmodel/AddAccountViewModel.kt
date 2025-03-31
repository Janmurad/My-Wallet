package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Account
import com.my.wallet.model.AccountType
import com.my.wallet.model.Currency
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAccountUiState())
    val uiState: StateFlow<AddAccountUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateType(type: AccountType) {
        _uiState.update { it.copy(type = type) }
    }

    fun updateCurrency(currency: Currency) {
        _uiState.update { it.copy(currency = currency) }
    }

    fun updateInitialBalance(balance: String) {
        _uiState.update { 
            it.copy(
                initialBalance = balance,
                isBalanceError = balance.toBigDecimalOrNull() == null
            )
        }
    }

    fun saveAccount(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        
        if (currentState.isValid) {
            viewModelScope.launch {
                try {
                    repository.addAccount(
                        Account(
                            name = currentState.name,
                            type = currentState.type,
                            balance = BigDecimal(currentState.initialBalance),
                            currency = currentState.currency
                        )
                    )
                    onSuccess()
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }
    }

    data class AddAccountUiState(
        val name: String = "",
        val type: AccountType = AccountType.CARD,
        val currency: Currency = Currency.RUB,
        val initialBalance: String = "0",
        val isBalanceError: Boolean = false,
        val error: String? = null
    ) {
        val isValid: Boolean
            get() = name.isNotBlank() && 
                    initialBalance.toBigDecimalOrNull() != null
    }
} 