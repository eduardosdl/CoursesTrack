package com.eduardosdl.coursestrack.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.util.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _login = MutableStateFlow<ViewModelState<String>>(ViewModelState.Idle)
    val login = _login.asStateFlow()

    fun login(email: String, password: String) {
        _login.value = ViewModelState.Loading
        viewModelScope.launch {
            repository.loginUser(email, password, onSuccess = {
                _login.value = ViewModelState.Success(it)
            }, onFailure = {
                _login.value = ViewModelState.Failure(it)
            })
        }
    }
}
