package com.my.wallet.model

import java.math.BigDecimal

data class WalletState(
    val accounts: List<Account> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val categories: List<TransactionCategory> = DefaultCategories.DEFAULT_CATEGORIES
) {
    val totalBalance: BigDecimal
        get() = accounts.fold(BigDecimal.ZERO) { acc, account -> acc + account.balance }
        
    fun getAccountById(id: String): Account? =
        accounts.find { it.id.toString() == id }
        
    fun getTransactionsByAccount(accountId: String): List<Transaction> =
        transactions.filter { it.accountId.toString() == accountId }
        
    fun getTransactionsByCategory(categoryId: String): List<Transaction> =
        transactions.filter { it.category.id.toString() == categoryId }
} 