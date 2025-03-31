package com.my.wallet.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Statistics : Screen("statistics")
    object Accounts : Screen("accounts")
    object AddTransaction : Screen("add_transaction")
    object AddAccount : Screen("add_account")
    object Settings : Screen("settings")
} 