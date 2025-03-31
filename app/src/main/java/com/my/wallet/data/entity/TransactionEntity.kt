package com.my.wallet.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.wallet.model.Transaction
import com.my.wallet.model.TransactionCategory
import com.my.wallet.model.TransactionType
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val type: String,
    val categoryId: String,
    val categoryName: String,
    val categoryType: String,
    val categoryIcon: String,
    val accountId: String,
    val date: Long,
    val note: String
) {
    fun toTransaction(): Transaction = Transaction(
        id = UUID.fromString(id),
        amount = BigDecimal(amount),
        type = TransactionType.valueOf(type),
        category = TransactionCategory(
            id = UUID.fromString(categoryId),
            name = categoryName,
            type = TransactionType.valueOf(categoryType),
            iconName = categoryIcon
        ),
        accountId = UUID.fromString(accountId),
        date = Date(date),
        note = note
    )

    companion object {
        fun fromTransaction(transaction: Transaction): TransactionEntity = TransactionEntity(
            id = transaction.id.toString(),
            amount = transaction.amount.toDouble(),
            type = transaction.type.name,
            categoryId = transaction.category.id.toString(),
            categoryName = transaction.category.name,
            categoryType = transaction.category.type.name,
            categoryIcon = transaction.category.iconName,
            accountId = transaction.accountId.toString(),
            date = transaction.date.time,
            note = transaction.note
        )
    }
} 