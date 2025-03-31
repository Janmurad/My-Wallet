package com.my.wallet.model

import java.math.BigDecimal
import java.util.Date
import java.util.UUID

data class Transaction(
    val id: UUID = UUID.randomUUID(),
    val amount: BigDecimal,
    val type: TransactionType,
    val category: TransactionCategory,
    val accountId: UUID,
    val date: Date = Date(),
    val note: String = ""
)

enum class TransactionType {
    INCOME,
    EXPENSE
}

data class TransactionCategory(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: TransactionType,
    val iconName: String
)

// Предустановленные категории
object DefaultCategories {
    val SALARY = TransactionCategory(name = "Зарплата", type = TransactionType.INCOME, iconName = "money")
    val FOOD = TransactionCategory(name = "Продукты", type = TransactionType.EXPENSE, iconName = "food")
    val TRANSPORT = TransactionCategory(name = "Транспорт", type = TransactionType.EXPENSE, iconName = "car")
    val ENTERTAINMENT = TransactionCategory(name = "Развлечения", type = TransactionType.EXPENSE, iconName = "movie")
    val SHOPPING = TransactionCategory(name = "Покупки", type = TransactionType.EXPENSE, iconName = "shopping")
    val HEALTH = TransactionCategory(name = "Здоровье", type = TransactionType.EXPENSE, iconName = "health")
    
    val DEFAULT_CATEGORIES = listOf(
        SALARY,
        FOOD,
        TRANSPORT,
        ENTERTAINMENT,
        SHOPPING,
        HEALTH
    )
} 