package com.eduardosdl.coursestrack.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    fun register(email: String, password: String) {
        _register.value = UiState.Loading
        repository.registerUser(email, password) {
            _register.value = it
        }
    }
}