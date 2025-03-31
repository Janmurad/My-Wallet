package com.my.wallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TransactionsViewModel(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    private val _selectedType = MutableStateFlow<TransactionType?>(null)
    private val _selectedDateRange = MutableStateFlow<Pair<Date, Date>?>(null)

    init {
        viewModelScope.launch {
            combine(
                repository.getAllTransactions(),
                _selectedType,
                _selectedDateRange
            ) { transactions, type, dateRange ->
                var filtered = transactions
                
                // Фильтруем по типу если выбран
                type?.let { t ->
                    filtered = filtered.filter { it.type == t }
                }
                
                // Фильтруем по датам если выбран диапазон
                dateRange?.let { (start, end) ->
                    filtered = filtered.filter { it.date in start..end }
                }

                TransactionsUiState(
                    transactions = filtered,
                    selectedType = type,
                    dateRange = dateRange
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setTypeFilter(type: TransactionType?) {
        _selectedType.value = type
    }

    fun setDateRange(start: Date, end: Date) {
        _selectedDateRange.value = start to end
    }

    fun clearFilters() {
        _selectedType.value = null
        _selectedDateRange.value = null
    }

    data class TransactionsUiState(
        val transactions: List<Transaction> = emptyList(),
        val selectedType: TransactionType? = null,
        val dateRange: Pair<Date, Date>? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    class Factory(private val repository: WalletRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionsViewModel(repository) as T
        }
    }
} 