package com.eduardosdl.coursestrack

import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository

) : ViewModel() {
    fun logout(result: () -> Unit) {
        authRepository.logout(result)
    }
}