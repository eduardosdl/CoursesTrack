package com.eduardosdl.coursestrack.ui.course

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.ui.uikit.Button
import com.eduardosdl.coursestrack.ui.uikit.TextField

@Composable
fun CourseForm(
    course: Course? = null,
    institutionsOptions: List<Institution>,
    mattersOptions: List<Matter>,
    onSave: (Course) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var selectedCourseType by remember { mutableStateOf("Horas") }
    var selectedInstitutionId by remember { mutableStateOf("") }
    var selectedMatterId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        TextField(
            value = name,
            label = "Nome do curso",
            isError = false,
            onValueChange = { name = it },
            errorResource = R.string.login,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )

        Text(
            text = "Como você deseja acompanhar a duração?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            RadioButtonWithText(
                text = "Horas",
                isSelected = selectedCourseType == "Horas",
                onClick = { selectedCourseType = "Horas" }
            )

            RadioButtonWithText(
                text = "Aulas",
                isSelected = selectedCourseType == "Aulas",
                onClick = { selectedCourseType = "Aulas" }
            )
        }

        TextField(
            value = duration,
            label = "Duração Total",
            isError = false,
            onValueChange = { duration = it },
            errorResource = R.string.login,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        DropdownTextField(
            label = "Instituição",
            value = institutionsOptions.firstOrNull { it.id == selectedInstitutionId }?.name.orEmpty(),
            options = institutionsOptions.map { it.id.orEmpty() to it.name },
            onValueChange = { selectedInstitutionId = it }
        )

        DropdownTextField(
            label = "Matéria",
            value = mattersOptions.firstOrNull { it.id == selectedMatterId }?.name.orEmpty(),
            options = mattersOptions.map { it.id.orEmpty() to it.name },
            onValueChange = { selectedMatterId = it }
        )

        Button(
            text = "Criar",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

    }
}

@Composable
fun DropdownTextField(
    label: String,
    value: String,
    options: List<Pair<String, String>>,
    onValueChange: (String) -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var textFieldWidth by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
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
            options.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onValueChange(id)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun RadioButtonWithText(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            onClick = onClick,
            indication = null,
            interactionSource = remember { MutableInteractionSource() })
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null
        )
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CourseFormPreview() {
    CourseForm(
        Course(
            name = "course name",
            userId = "user id",
            durationType = "duration type",
            duration = 0,
            progress = 0,
            institutionName = "institution name",
            matterName = "matter name"
        ),
        institutionsOptions = listOf(Institution("institution id", "institution name", "user id")),
        mattersOptions = listOf(Matter("matter id", "matter name", "user id"))
    ) {}
}