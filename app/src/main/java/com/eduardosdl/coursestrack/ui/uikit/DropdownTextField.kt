package com.eduardosdl.coursestrack.ui.uikit

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.data.model.Category

@Composable
fun <T: Category> DropdownTextField(
    label: String,
    value: String,
    options: List<T>,
    @StringRes errorResource: Int?,
    disabled: Boolean = false,
    onValueChange: (T) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var textFieldWidth by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                label = { Text(label) },
                isError = errorResource != null,
                readOnly = true,
                enabled = !disabled,
                trailingIcon = {
                    IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(if (isDropdownExpanded) 180f else 0f)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .focusRequester(focusRequester)
                    .onGloballyPositioned { coordinates ->
                        textFieldWidth = coordinates.size.width
                    }
            )

            AnimatedVisibility(visible = errorResource != null) {
                Text(
                    text = errorResource?.let { stringResource(it) }.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    isDropdownExpanded = !isDropdownExpanded
                    if (isDropdownExpanded) {
                        focusRequester.requestFocus()
                    }
                }
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldWidth.toDp() })
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onValueChange(it)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}