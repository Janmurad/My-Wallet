package com.my.wallet.model

import java.math.BigDecimal
import java.util.UUID

data class Account(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val type: AccountType,
    val balance: BigDecimal = BigDecimal.ZERO,
    val currency: Currency = Currency.RUB,
    val iconName: String = ""
)

enum class AccountType {
    CASH,
    CARD,
    SAVINGS
}

enum class Currency(val symbol: String, val code: String) {
    RUB("₽", "RUB"),
    USD("$", "USD"),
    EUR("€", "EUR"),
    TMT("m", "TMT")
} 