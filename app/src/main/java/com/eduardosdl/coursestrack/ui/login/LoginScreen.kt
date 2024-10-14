package com.eduardosdl.coursestrack.ui.login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.ui.shared.Button
import com.eduardosdl.coursestrack.ui.shared.OutlinedPasswordField
import com.eduardosdl.coursestrack.ui.shared.WordMark
import com.eduardosdl.coursestrack.util.ViewModelState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.login.collectAsState()

    LoginContent(
        state = state,
        onLogin = viewModel::login,
        onNavigateToRegister = onNavigateToRegister
    )

    if (state is ViewModelState.Success) {
        onLoginSuccess()
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
fun LoginContent(
    state: ViewModelState<String>,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedPasswordField(
                password,
                onPasswordChange = { password = it },
                label = "Senha",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Esqueceu a senha ?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Equeceu a senha",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = { onLogin(email, password) },
                text = "Entrar",
                modifier = Modifier.fillMaxWidth(),
                isLoading = state is ViewModelState.Loading
            )


            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ainda não possui uma conta? ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Criar Conta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onNavigateToRegister()
                    }
                )

            }
        }
    }
}