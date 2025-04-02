package com.my.wallet

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.my.wallet.navigation.Screen
import com.my.wallet.screens.*
import com.my.wallet.ui.theme.MyWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                WalletApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = when (currentDestination?.route) {
        Screen.Statistics.route -> Screen.Statistics
        Screen.Accounts.route -> Screen.Accounts
        Screen.AddTransaction.route -> Screen.AddTransaction
        Screen.Categories.route -> Screen.Categories
        Screen.Calendar.route -> Screen.Calendar
        else -> Screen.Home
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != Screen.AddTransaction) {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.app_name)) },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentScreen !in listOf(Screen.AddTransaction, Screen.AddAccount)) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Home,
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(R.string.home)) }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Statistics,
                        onClick = { navController.navigate(Screen.Statistics.route) },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text(stringResource(R.string.statistics)) }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Calendar,
                        onClick = { navController.navigate(Screen.Calendar.route) },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        label = { Text(stringResource(R.string.calendar)) }
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Categories,
                        onClick = { navController.navigate(Screen.Categories.route) },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        label = { Text(stringResource(R.string.categories)) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentScreen != Screen.AddTransaction) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction.route) }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_transaction)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Statistics.route) { StatisticsScreen() }
            composable(Screen.Accounts.route) { 
                AccountsScreen(
                    onAddAccountClick = { navController.navigate(Screen.AddAccount.route) }
                )
            }
            composable(Screen.AddTransaction.route) { 
                AddTransactionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAddAccountClick = { navController.navigate(Screen.AddAccount.route) }
                )
            }
            composable(Screen.AddAccount.route) {
                AddAccountScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Categories.route) {
                CategoriesScreen()
            }
            composable(Screen.Calendar.route) {
                CalendarScreen()
            }
        }
    }
}