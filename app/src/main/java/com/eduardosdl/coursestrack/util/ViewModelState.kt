package com.eduardosdl.coursestrack.util

sealed class ViewModelState<out T> {
    data object Idle : ViewModelState<Nothing>()
    data object Loading : ViewModelState<Nothing>()
    data class Success<out T>(val data: T) : ViewModelState<T>()
    data class Failure(val message: String) : ViewModelState<Nothing>()
}