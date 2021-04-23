package vn.teko.cart.domain.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object PlatformDateBridge {
    fun getCurrentDate(): Instant {
        return Clock.System.now()
    }

    fun getTimeInDayString(date: Instant): String {
        return date.toString()
    }

}
