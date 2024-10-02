package com.eduardosdl.coursestrack.util

fun String.limitTitleLength(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength) + "..."
    } else {
        this
    }
}
