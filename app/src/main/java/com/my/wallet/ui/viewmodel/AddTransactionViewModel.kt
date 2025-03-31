package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllAccounts().collect { accounts ->
                _uiState.update { it.copy(accounts = accounts) }
            }
        }
    }

    fun updateAmount(amount: String) {
        _uiState.update { 
            it.copy(
                amount = amount,
                isAmountError = amount.toBigDecimalOrNull() == null
            )
        }
    }

    fun updateType(type: TransactionType) {
        _uiState.update { 
            it.copy(
                type = type,
                categories = DefaultCategories.DEFAULT_CATEGORIES.filter { cat -> cat.type == type }
            )
        }
    }

    fun updateCategory(category: TransactionCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateAccount(account: Account) {
        _uiState.update { it.copy(selectedAccount = account) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        
        if (currentState.isValid) {
            viewModelScope.launch {
                try {
                    repository.addTransaction(
                        Transaction(
                            amount = BigDecimal(currentState.amount),
                            type = currentState.type,
                            category = currentState.selectedCategory!!,
                            accountId = currentState.selectedAccount!!.id,
                            note = currentState.note
                        )
                    )
                    onSuccess()
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }
    }

    data class AddTransactionUiState(
        val amount: String = "",
        val isAmountError: Boolean = false,
        val type: TransactionType = TransactionType.EXPENSE,
        val categories: List<TransactionCategory> = DefaultCategories.DEFAULT_CATEGORIES.filter { it.type == TransactionType.EXPENSE },
        val selectedCategory: TransactionCategory? = null,
        val accounts: List<Account> = emptyList(),
        val selectedAccount: Account? = null,
        val note: String = "",
        val error: String? = null
    ) {
        val isValid: Boolean
            get() = amount.toBigDecimalOrNull() != null && 
                    selectedCategory != null && 
                    selectedAccount != null
    }
} 