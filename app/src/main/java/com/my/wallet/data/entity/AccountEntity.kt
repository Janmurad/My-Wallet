package com.my.wallet.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.wallet.model.Account
import com.my.wallet.model.AccountType
import com.my.wallet.model.Currency
import java.math.BigDecimal
import java.util.UUID

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val balance: Double,
    val currency: String,
    val iconName: String
) {
    fun toAccount(): Account = Account(
        id = UUID.fromString(id),
        name = name,
        type = AccountType.valueOf(type),
        balance = BigDecimal(balance),
        currency = Currency.valueOf(currency),
        iconName = iconName
    )

    companion object {
        fun fromAccount(account: Account): AccountEntity = AccountEntity(
            id = account.id.toString(),
            name = account.name,
            type = account.type.name,
            balance = account.balance.toDouble(),
            currency = account.currency.name,
            iconName = account.iconName
        )
    }
} 