package com.my.wallet.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.wallet.R
import com.my.wallet.model.AccountType
import com.my.wallet.model.Currency
import com.my.wallet.ui.LocalWalletApplication
import com.my.wallet.ui.viewmodel.AddAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddAccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isTypeMenuExpanded by remember { mutableStateOf(false) }
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_account)) },
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
            // Название счета
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text(stringResource(R.string.account_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Тип счета
            ExposedDropdownMenuBox(
                expanded = isTypeMenuExpanded,
                onExpandedChange = { isTypeMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = stringResource(
                        when (uiState.type) {
                            AccountType.CASH -> R.string.account_type_cash
                            AccountType.CARD -> R.string.account_type_card
                            AccountType.SAVINGS -> R.string.account_type_savings
                        }
                    ),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.account_type)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isTypeMenuExpanded,
                    onDismissRequest = { isTypeMenuExpanded = false }
                ) {
                    AccountType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    stringResource(
                                        when (type) {
                                            AccountType.CASH -> R.string.account_type_cash
                                            AccountType.CARD -> R.string.account_type_card
                                            AccountType.SAVINGS -> R.string.account_type_savings
                                        }
                                    )
                                )
                            },
                            onClick = {
                                viewModel.updateType(type)
                                isTypeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Валюта
            ExposedDropdownMenuBox(
                expanded = isCurrencyMenuExpanded,
                onExpandedChange = { isCurrencyMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = "${uiState.currency.code} (${uiState.currency.symbol})",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(stringResource(R.string.currency)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCurrencyMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isCurrencyMenuExpanded,
                    onDismissRequest = { isCurrencyMenuExpanded = false }
                ) {
                    Currency.values().forEach { currency ->
                        DropdownMenuItem(
                            text = { Text("${currency.code} (${currency.symbol})") },
                            onClick = {
                                viewModel.updateCurrency(currency)
                                isCurrencyMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Начальный баланс
            OutlinedTextField(
                value = uiState.initialBalance,
                onValueChange = { viewModel.updateInitialBalance(it) },
                label = { Text(stringResource(R.string.initial_balance)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.isBalanceError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка сохранения
            Button(
                onClick = { viewModel.saveAccount(onSuccess = onNavigateBack) },
                enabled = uiState.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
} 