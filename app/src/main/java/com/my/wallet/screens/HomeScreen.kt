package com.my.wallet.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.wallet.R
import com.my.wallet.model.Account
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionType
import com.my.wallet.ui.LocalWalletApplication
import com.my.wallet.ui.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Общий баланс
        Text(
            text = "${uiState.totalBalance} ₽",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.current_balance),
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Список счетов
        Text(
            text = stringResource(R.string.accounts),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        AccountsList(accounts = uiState.accounts)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Последние транзакции
        Text(
            text = stringResource(R.string.recent_transactions),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TransactionsList(transactions = uiState.recentTransactions)
    }
}

@Composable
private fun AccountsList(accounts: List<Account>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        items(accounts) { account ->
            AccountItem(account = account)
        }
    }
}

@Composable
private fun AccountItem(account: Account) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = account.name)
                Text(
                    text = account.type.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${account.balance} ${account.currency.symbol}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun TransactionsList(transactions: List<Transaction>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction = transaction)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = transaction.category.name)
                Text(
                    text = dateFormat.format(transaction.date),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${if (transaction.type == TransactionType.EXPENSE) "-" else "+"}${transaction.amount} ₽",
                style = MaterialTheme.typography.titleMedium,
                color = when (transaction.type) {
                    TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
                    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
} 