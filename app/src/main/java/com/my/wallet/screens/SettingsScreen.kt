package com.my.wallet.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        // Язык
        ExposedDropdownMenuBox(
            expanded = isLanguageMenuExpanded,
            onExpandedChange = { isLanguageMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = stringResource(
                    when (uiState.language) {
                        Language.SYSTEM -> R.string.language_system
                        Language.RU -> R.string.language_ru
                        Language.EN -> R.string.language_en
                    }
                ),
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.language)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLanguageMenuExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isLanguageMenuExpanded,
                onDismissRequest = { isLanguageMenuExpanded = false }
            ) {
                Language.values().forEach { language ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(
                                    when (language) {
                                        Language.SYSTEM -> R.string.language_system
                                        Language.RU -> R.string.language_ru
                                        Language.EN -> R.string.language_en
                                    }
                                )
                            )
                        },
                        onClick = {
                            viewModel.updateLanguage(language)
                            isLanguageMenuExpanded = false
                        }
                    )
                }
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
} 