package com.eduardosdl.coursestrack.ui.register

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.util.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _register = MutableStateFlow<ViewModelState<String>>(ViewModelState.Idle)
    val register: StateFlow<ViewModelState<String>> = _register

    private val _formState = MutableStateFlow(RegisterState())
    val formState: StateFlow<RegisterState> = _formState

    fun onFormChange(event: FieldEvent) {
        when (event) {
            is FieldEvent.EmailChanged -> {
                _formState.value = _formState.value.copy(email = event.email)
                validateEmail()
            }

            is FieldEvent.PasswordChanged -> {
                _formState.value = _formState.value.copy(password = event.password)
                validatePassword()
            }

            is FieldEvent.ConfirmPasswordChanged -> {
                _formState.value = _formState.value.copy(confirmPassword = event.confirmPassword)
                validateConfirmPassword()
            }
        }
    }

    fun registerUser() {
        if (!validate()) return

        _register.value = ViewModelState.Loading
        repository.registerUser(
            _formState.value.email,
            _formState.value.password,
            onSuccess = {
                _register.value = ViewModelState.Success(it)
            }, onFailure = {
                _register.value = ViewModelState.Failure(it)
            })
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!validateEmail()) {
            isValid = false
        }

        if (!validatePassword()) {
            isValid = false
        }

        if (!validateConfirmPassword()) {
            isValid = false
        }

        return isValid
    }

    private fun validateEmail(): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_formState.value.email).matches()) {
            _formState.value = _formState.value.copy(emailError = R.string.email_invalid)
            return false
        }
        _formState.value = _formState.value.copy(emailError = null)
        return true
    }

    private fun validatePassword(): Boolean {
        if (_formState.value.password.length < 6) {
            _formState.value = _formState.value.copy(passwordError = R.string.password_length)
            return false
        }
        _formState.value = _formState.value.copy(passwordError = null)
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        if (_formState.value.confirmPassword != _formState.value.password) {
            _formState.value =
                _formState.value.copy(confirmPasswordError = R.string.password_not_match)
            return false
        }
        _formState.value = _formState.value.copy(confirmPasswordError = null)
        return true
    }

    sealed class FieldEvent {
        data class EmailChanged(val email: String) : FieldEvent()
        data class PasswordChanged(val password: String) : FieldEvent()
        data class ConfirmPasswordChanged(val confirmPassword: String) : FieldEvent()
    }

    data class RegisterState(
        val email: String = "",
        @StringRes val emailError: Int? = null,
        val password: String = "",
        @StringRes val passwordError: Int? = null,
        val confirmPassword: String = "",
        @StringRes val confirmPasswordError: Int? = null,
    )
}