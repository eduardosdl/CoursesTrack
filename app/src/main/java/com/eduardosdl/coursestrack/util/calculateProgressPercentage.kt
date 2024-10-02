fun calculateProgressPercentage(duration: Long?, progress: Long?): Int {
    if (duration == null || duration == 0L || progress == null) {
        return 0
    }
    return ((progress.toDouble() / duration) * 100).toInt()
}
