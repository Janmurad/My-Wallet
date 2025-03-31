package com.my.wallet.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.my.wallet.R

@Composable
fun StatisticsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.statistics),
            style = MaterialTheme.typography.headlineMedium
        )
        // TODO: Графики и статистика будут здесь
    }
} 