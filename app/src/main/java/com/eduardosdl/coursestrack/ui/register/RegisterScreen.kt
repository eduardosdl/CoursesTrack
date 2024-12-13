package com.eduardosdl.coursestrack.ui.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.ui.uikit.Button
import com.eduardosdl.coursestrack.ui.uikit.OutlinedPasswordField
import com.eduardosdl.coursestrack.ui.uikit.TextField
import com.eduardosdl.coursestrack.ui.uikit.WordMark
import com.eduardosdl.coursestrack.util.ViewModelState

@Composable
fun RegisterRouter(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.register.collectAsState()

    RegisterScreen(
        state = state,
        handleRegister = viewModel::registerUser,
        onNavigateToLogin = onNavigateToLogin,
        viewModel = viewModel
    )

    if (state is ViewModelState.Success) {
        onRegisterSuccess()
    }

    if (state is ViewModelState.Failure) {
        Toast.makeText(
            LocalContext.current,
            (state as ViewModelState.Failure).message,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun RegisterScreen(
    state: ViewModelState<String>,
    handleRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel
) {
    val formState by viewModel.formState.collectAsState()

    Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WordMark()

            Spacer(modifier = Modifier.height(64.dp))

            TextField(
                value = formState.email,
                label = stringResource(R.string.email),
                isError = formState.emailError != null,
                onValueChange = { viewModel.onEvent(RegisterViewModel.RegisterEvent.EmailChanged(it)) },
                errorResource = formState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedPasswordField(
                value = formState.password,
                label = stringResource(R.string.password),
                isError = formState.passwordError != null,
                onValueChange = {
                    viewModel.onEvent(
                        RegisterViewModel.RegisterEvent.PasswordChanged(
                            it
                        )
                    )
                },
                errorResource = formState.passwordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedPasswordField(
                value = formState.confirmPassword,
                label = stringResource(R.string.password_confirm),
                isError = formState.confirmPasswordError != null,
                onValueChange = {
                    viewModel.onEvent(
                        RegisterViewModel.RegisterEvent.ConfirmPasswordChanged(
                            it
                        )
                    )
                },
                errorResource = formState.confirmPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(onSend = {
                    handleRegister()
                })
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = { handleRegister() },
                text = stringResource(R.string.create_account),
                modifier = Modifier.fillMaxWidth(),
                isLoading = state is ViewModelState.Loading
            )


            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.have_account_question),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onNavigateToLogin()
                    }
                )

            }
        }
    }
}
