package com.cafeinlove14h.cartcompose.utils

import java.text.DecimalFormat
import java.util.*

object CurrencyUtils {
    @JvmStatic
    fun formatDiscount(
        amount: Double,
        hasCurrencySymbol: Boolean = true,
        locale: Locale = LocaleUtils.getDefaultLocale()
    ): String {
        return if (amount == 0.0) {
            formatMoney(amount, hasCurrencySymbol, locale)
        } else {
            "-${formatMoney(amount, hasCurrencySymbol, locale)}"
        }
    }

    @JvmStatic
    fun formatMoney(
        amount: Double,
        hasCurrencySymbol: Boolean = true,
        locale: Locale = LocaleUtils.getDefaultLocale()
    ): String {
        val formatter = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        if (!hasCurrencySymbol) {
            formatter.decimalFormatSymbols = formatter.decimalFormatSymbols.apply {
                currencySymbol = ""
            }
        }

        return formatter.format(amount)
            .trim()
    }
}