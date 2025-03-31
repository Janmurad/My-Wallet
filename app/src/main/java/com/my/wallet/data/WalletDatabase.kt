package com.my.wallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.my.wallet.data.dao.AccountDao
import com.my.wallet.data.dao.TransactionDao
import com.my.wallet.data.entity.AccountEntity
import com.my.wallet.data.entity.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: WalletDatabase? = null

        fun getDatabase(context: Context): WalletDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WalletDatabase::class.java,
                    "wallet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 