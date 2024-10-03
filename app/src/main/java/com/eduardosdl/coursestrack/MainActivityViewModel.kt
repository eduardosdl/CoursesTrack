package com.eduardosdl.coursestrack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _deleteUser = MutableLiveData<UiState<String>>()
    val deleteUser: LiveData<UiState<String>> get() = _deleteUser

    fun logout(result: () -> Unit) {
        authRepository.logout(result)
    }

    fun deleteUser(result: (UiState<String>) -> Unit) = viewModelScope.launch {
        authRepository.deleteUser {
            result.invoke(it)
        }
    }
}
