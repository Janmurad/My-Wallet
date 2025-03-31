package com.my.wallet.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTransactions()
                .map { transactions -> 
                    val currentMonth = YearMonth.now()
                    val monthTransactions = transactions.filter { 
                        YearMonth.from(it.date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()) == currentMonth 
                    }
                    
                    StatisticsUiState(
                        monthlyIncome = monthTransactions
                            .filter { it.type == TransactionType.INCOME }
                            .sumOf { it.amount },
                        monthlyExpense = monthTransactions
                            .filter { it.type == TransactionType.EXPENSE }
                            .sumOf { it.amount },
                        dailyExpenses = calculateDailyExpenses(monthTransactions),
                        categoryExpenses = calculateCategoryExpenses(monthTransactions)
                    )
                }
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { state -> _uiState.value = state }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDailyExpenses(transactions: List<Transaction>): Map<LocalDate, BigDecimal> {
        return transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { 
                it.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate() 
            }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }

    private fun calculateCategoryExpenses(transactions: List<Transaction>): Map<String, BigDecimal> {
        return transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category.name }
            .mapValues { (_, transactions) -> transactions.sumOf { it.amount } }
    }

    data class StatisticsUiState(
        val monthlyIncome: BigDecimal = BigDecimal.ZERO,
        val monthlyExpense: BigDecimal = BigDecimal.ZERO,
        val dailyExpenses: Map<LocalDate, BigDecimal> = emptyMap(),
        val categoryExpenses: Map<String, BigDecimal> = emptyMap(),
        val error: String? = null
    )
} 