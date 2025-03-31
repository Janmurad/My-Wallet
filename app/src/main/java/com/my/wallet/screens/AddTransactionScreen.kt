package com.my.wallet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.wallet.R
import com.my.wallet.model.AccountType
import com.my.wallet.model.TransactionType
import com.my.wallet.ui.LocalWalletApplication
import com.my.wallet.ui.viewmodel.AddTransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    onAddAccountClick: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    var isAccountMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_transaction)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Сумма
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { viewModel.updateAmount(it) },
                label = { Text(stringResource(R.string.amount)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.isAmountError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Тип транзакции
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TransactionType.values().forEach { type ->
                    FilterChip(
                        selected = uiState.type == type,
                        onClick = { viewModel.updateType(type) },
                        label = { Text(stringResource(if (type == TransactionType.INCOME) R.string.income else R.string.expense)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Категория
            ExposedDropdownMenuBox(
                expanded = isCategoryMenuExpanded,
                onExpandedChange = { isCategoryMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.selectedCategory?.name ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.category)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false }
                ) {
                    uiState.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.updateCategory(category)
                                isCategoryMenuExpanded = false
                            },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier.size(40.dp),
                                    painter = painterResource(R.drawable.category),
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Счет
            Column {
                ExposedDropdownMenuBox(
                    expanded = isAccountMenuExpanded,
                    onExpandedChange = { isAccountMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = uiState.selectedAccount?.name ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text(stringResource(R.string.account)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAccountMenuExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isAccountMenuExpanded,
                        onDismissRequest = { isAccountMenuExpanded = false }
                    ) {
                        if (uiState.accounts.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.no_accounts)) },
                                onClick = { 
                                    isAccountMenuExpanded = false
                                    onAddAccountClick()
                                }
                            )
                        } else {
                            uiState.accounts.forEach { account ->
                                DropdownMenuItem(
                                    text = { 
                                        Column {
                                            Text(account.name)
                                            Text(
                                                "${account.balance} ${account.currency.symbol}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.updateAccount(account)
                                        isAccountMenuExpanded = false
                                    },
                                    leadingIcon = {
                                        Image(
                                            modifier = Modifier.size(40.dp),
                                            painter = when(account.type) {
                                                AccountType.CASH ->painterResource(R.drawable.money)
                                                AccountType.CARD -> painterResource(R.drawable.credit_card)
                                                AccountType.SAVINGS -> painterResource(R.drawable.savings)
                                            },
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                            
                            Divider()
                            
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.add_account)) },
                                onClick = { 
                                    isAccountMenuExpanded = false
                                    onAddAccountClick()
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Заметка
            OutlinedTextField(
                value = uiState.note,
                onValueChange = { viewModel.updateNote(it) },
                label = { Text(stringResource(R.string.note)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка сохранения
            Button(
                onClick = { viewModel.saveTransaction(onSuccess = onNavigateBack) },
                enabled = uiState.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
} 