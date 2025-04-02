package com.my.wallet.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.wallet.data.repository.WalletRepository
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        selectDate(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            repository.getAllTransactions()
                .map { transactions ->
                    val dayTransactions = transactions.filter {
                        it.date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate() == date
                    }
                    
                    CalendarUiState(
                        selectedDate = date,
                        dayTransactions = dayTransactions,
                        dayIncome = dayTransactions
                            .filter { it.type == TransactionType.INCOME }
                            .sumOf { it.amount },
                        dayExpense = dayTransactions
                            .filter { it.type == TransactionType.EXPENSE }
                            .sumOf { it.amount }
                    )
                }
                .collect { state -> _uiState.value = state }
        }
    }

    data class CalendarUiState(
        val selectedDate: LocalDate = LocalDate.now(),
        val dayTransactions: List<Transaction> = emptyList(),
        val dayIncome: BigDecimal = BigDecimal.ZERO,
        val dayExpense: BigDecimal = BigDecimal.ZERO
    )
} 