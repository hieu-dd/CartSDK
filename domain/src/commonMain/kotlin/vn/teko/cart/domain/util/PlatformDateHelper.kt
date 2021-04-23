package vn.teko.cart.domain.util

import kotlinx.datetime.Instant
import vn.teko.cart.domain.model.PromotionEntity


internal fun Instant.isInDateRange(
    start: Instant?,
    end: Instant?
): Boolean {
    return (start == null || this.compareTo(start) >= 0)
            && (end == null || this.compareTo(end) <= 0)
}

internal fun Instant.isInTimeRange(
    start: String,
    end: String
): Boolean {
    val timeInDayString = PlatformDateBridge.getTimeInDayString(this)
    return timeInDayString in start..end
}

internal fun Instant.isInTimeRanges(
    timeRanges: List<PromotionEntity.TimeRangeEntity>?
): Boolean {
    if (timeRanges.isNullOrEmpty()) return true

    val timeInDayString = PlatformDateBridge.getTimeInDayString(this)

    return timeRanges.any { timeInDayString in it.start..it.end }
}