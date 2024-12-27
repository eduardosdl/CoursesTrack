package com.eduardosdl.coursestrack.util

private const val PERCENTAGE_MULTIPLIER = 100

fun calculateProgressPercentage(duration: Long?, progress: Long?): Int {
    if (duration == null || duration == 0L || progress == null) {
        return 0
    }
    return ((progress.toDouble() / duration) * PERCENTAGE_MULTIPLIER).toInt()
}
