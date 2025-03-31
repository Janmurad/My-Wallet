package com.my.wallet.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.my.wallet.R

@Composable
fun AccountsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.accounts),
            style = MaterialTheme.typography.headlineMedium
        )
        // TODO: Список счетов будет здесь
    }
} 