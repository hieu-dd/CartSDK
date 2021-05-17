package com.cafeinlove14h.cartcompose.utils

import java.text.NumberFormat
import java.util.*

object NumberUtils {
    @JvmStatic
    fun formatInteger(
        number: Long,
        locale: Locale = LocaleUtils.getDefaultLocale()
    ): String {
        val formatter = NumberFormat.getIntegerInstance(locale)
        return formatter.format(number).trim()
    }
}