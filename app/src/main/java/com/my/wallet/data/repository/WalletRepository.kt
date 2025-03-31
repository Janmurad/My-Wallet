package com.my.wallet.data.repository

import com.my.wallet.data.WalletDatabase
import com.my.wallet.data.entity.AccountEntity
import com.my.wallet.data.entity.TransactionEntity
import com.my.wallet.model.Account
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class WalletRepository(private val database: WalletDatabase) {
    
    // Операции со счетами
    fun getAllAccounts(): Flow<List<Account>> =
        database.accountDao().getAllAccounts()
            .map { entities -> entities.map { it.toAccount() } }

    suspend fun getAccountById(id: String): Account? =
        database.accountDao().getAccountById(id)?.toAccount()

    suspend fun addAccount(account: Account) {
        database.accountDao().insertAccount(AccountEntity.fromAccount(account))
    }

    suspend fun deleteAccount(account: Account) {
        database.accountDao().deleteAccount(AccountEntity.fromAccount(account))
    }

    // Операции с транзакциями
    fun getAllTransactions(): Flow<List<Transaction>> =
        database.transactionDao().getAllTransactions()
            .map { entities -> entities.map { it.toTransaction() } }

    fun getTransactionsByAccount(accountId: String): Flow<List<Transaction>> =
        database.transactionDao().getTransactionsByAccount(accountId)
            .map { entities -> entities.map { it.toTransaction() } }

    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> =
        database.transactionDao().getAllTransactions()
            .map { entities -> 
                entities
                    .map { it.toTransaction() }
                    .filter { it.date in startDate..endDate }
            }

    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        database.transactionDao().getAllTransactions()
            .map { entities -> 
                entities
                    .map { it.toTransaction() }
                    .filter { it.type == type }
            }

    suspend fun addTransaction(transaction: Transaction) {
        database.run {
            // Добавляем транзакцию
            transactionDao().insertTransaction(TransactionEntity.fromTransaction(transaction))
            
            // Обновляем баланс счета
            val amount = when (transaction.type) {
                TransactionType.INCOME -> transaction.amount
                TransactionType.EXPENSE -> transaction.amount.negate()
            }
            accountDao().updateBalance(
                accountId = transaction.accountId.toString(),
                amount = amount.toDouble()
            )
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        database.run {
            // Удаляем транзакцию
            transactionDao().deleteTransaction(TransactionEntity.fromTransaction(transaction))
            
            // Отменяем изменение баланса
            val amount = when (transaction.type) {
                TransactionType.INCOME -> transaction.amount.negate()
                TransactionType.EXPENSE -> transaction.amount
            }
            accountDao().updateBalance(
                accountId = transaction.accountId.toString(),
                amount = amount.toDouble()
            )
        }
    }

    // Статистика
    fun getIncomeByPeriod(startDate: Date, endDate: Date): Flow<List<Transaction>> =
        getAllTransactions().map { transactions ->
            transactions.filter { 
                it.type == TransactionType.INCOME && 
                it.date in startDate..endDate 
            }
        }

    fun getExpensesByPeriod(startDate: Date, endDate: Date): Flow<List<Transaction>> =
        getAllTransactions().map { transactions ->
            transactions.filter { 
                it.type == TransactionType.EXPENSE && 
                it.date in startDate..endDate 
            }
        }

    fun getTransactionsByCategory(categoryId: String): Flow<List<Transaction>> =
        getAllTransactions().map { transactions ->
            transactions.filter { it.category.id.toString() == categoryId }
        }
} 