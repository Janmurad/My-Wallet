package com.my.wallet.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.my.wallet.WalletApplication

val LocalWalletApplication = staticCompositionLocalOf<WalletApplication> {
    error("No WalletApplication provided")
} 