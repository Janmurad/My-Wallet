package com.my.wallet.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.wallet.R
import com.my.wallet.ui.viewmodel.StatisticsViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import java.time.format.DateTimeFormatter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Итоги месяца
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.monthly_summary),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.monthly_income),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${uiState.monthlyIncome} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.monthly_expense),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${uiState.monthlyExpense} ₽",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // График расходов по дням
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.daily_expenses),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val entries = uiState.dailyExpenses
                    .toList()
                    .mapIndexed { index, (_, amount) ->
                        FloatEntry(
                            x = index.toFloat(),
                            y = amount.toFloat()
                        )
                    }

                if (entries.isNotEmpty()) {
                    ProvideChartStyle {
                        Chart(
                            chart = lineChart(),
                            model = entryModelOf(entries),
                            startAxis = rememberStartAxis(),
                            bottomAxis = rememberBottomAxis(
                                valueFormatter = { value, _ ->
                                    val date = uiState.dailyExpenses.keys.toList()[value.toInt()]
                                    date.format(DateTimeFormatter.ofPattern("dd.MM"))
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }
        }

        // График расходов по категориям
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.category_expenses),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val entries = uiState.categoryExpenses
                    .toList()
                    .mapIndexed { index, (_, amount) ->
                        FloatEntry(
                            x = index.toFloat(),
                            y = amount.toFloat()
                        )
                    }

                if (entries.isNotEmpty()) {
                    ProvideChartStyle {
                        Chart(
                            chart = columnChart(),
                            model = entryModelOf(entries),
                            startAxis = rememberStartAxis(),
                            bottomAxis = rememberBottomAxis(
                                valueFormatter = { value, _ ->
                                    uiState.categoryExpenses.keys.toList()[value.toInt()]
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }
        }
    }
} 