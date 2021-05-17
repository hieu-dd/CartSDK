package com.cafeinlove14h.cartcompose.utils

import java.util.*


object LocaleUtils {
    private val VIETNAM = Locale("vi", "VN")

    fun getDefaultLocale(): Locale {
        return VIETNAM // should change to Locale.getDefault() if we want to use general currency
    }
}