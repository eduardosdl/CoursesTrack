package com.eduardosdl.coursestrack.ui.uikit

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: String,
    label: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    @StringRes errorResource: Int? = null,
    errorMessage: String = "",
    onRemoveErrors: () -> Unit = {},
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                onRemoveErrors()
            },
            label = { Text(text = label) },
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            isError = isError,
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = errorResource?.run { stringResource(this) } ?: errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp, start = 16.dp)
                    .align(Alignment.Start)
            )
        }
    }
}