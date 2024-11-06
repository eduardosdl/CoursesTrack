package com.eduardosdl.coursestrack.ui.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.R

@Composable
fun OutlinedPasswordField(
    value: String,
    label: String,
    isError: Boolean,
    errorMessage: String,
    onRemoveErrors: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            label = { Text(label) },
            onValueChange = {
                onValueChange(it)
                onRemoveErrors()
            },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val iconRes = if (isPasswordVisible)
                    R.drawable.ic_visibility_24dp
                else
                    R.drawable.ic_visibility_off_24dp

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription =
                        if (isPasswordVisible)
                            stringResource(R.string.hide_password)
                        else
                            stringResource(R.string.show_password)
                    )
                }
            },
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp, start = 16.dp)
                    .align(Alignment.Start)
            )
        }
    }
}

