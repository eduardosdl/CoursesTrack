package com.eduardosdl.coursestrack.ui.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.eduardosdl.coursestrack.R

@Composable
fun OutlinedPasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val iconRes = if (isPasswordVisible)
                R.drawable.ic_visibility_24dp
            else
                R.drawable.ic_visibility_off_24dp

            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = if (isPasswordVisible) "Esconder senha" else "Mostrar senha"
                )
            }
        },
    )
}

