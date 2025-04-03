package com.my.wallet.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.wallet.R
import com.my.wallet.model.Currency
import com.my.wallet.ui.viewmodel.SettingsViewModel
import com.my.wallet.ui.viewmodel.SettingsViewModel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isThemeMenuExpanded by remember { mutableStateOf(false) }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Тема
        ExposedDropdownMenuBox(
            expanded = isThemeMenuExpanded,
            onExpandedChange = { isThemeMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = stringResource(
                    when (uiState.theme) {
                        Theme.SYSTEM -> R.string.theme_system
                        Theme.LIGHT -> R.string.theme_light
                        Theme.DARK -> R.string.theme_dark
                    }
                ),
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.theme)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isThemeMenuExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isThemeMenuExpanded,
                onDismissRequest = { isThemeMenuExpanded = false }
            ) {
                Theme.values().forEach { theme ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(
                                    when (theme) {
                                        Theme.SYSTEM -> R.string.theme_system
                                        Theme.LIGHT -> R.string.theme_light
                                        Theme.DARK -> R.string.theme_dark
                                    }
                                )
                            )
                        },
                        onClick = {
                            viewModel.updateTheme(theme)
                            isThemeMenuExpanded = false
                        }
                    )
                }
            }
        }

        // Выбор языка
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showLanguageDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.language),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = uiState.availableLanguages.find { 
                            it.code == uiState.selectedLanguage 
                        }?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }

        // Валюта по умолчанию
        ExposedDropdownMenuBox(
            expanded = isCurrencyMenuExpanded,
            onExpandedChange = { isCurrencyMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = "${uiState.defaultCurrency.code} (${uiState.defaultCurrency.symbol})",
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.default_currency)) },
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
                            viewModel.updateDefaultCurrency(currency)
                            isCurrencyMenuExpanded = false
                        }
                    )
                }
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.select_language)) },
            text = {
                Column {
                    uiState.availableLanguages.forEach { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setLanguage(language.code)
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(language.name)
                            if (language.code == uiState.selectedLanguage) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
} 