package com.eduardosdl.coursestrack.ui.course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.ui.uikit.Button
import com.eduardosdl.coursestrack.ui.uikit.DropdownTextField
import com.eduardosdl.coursestrack.ui.uikit.TextField

@Composable
fun CourseForm(
    mattersOptions: List<Matter>,
    institutionsOptions: List<Institution>,
    formState: CourseViewModel.CourseState,
    onFormChange: (CourseViewModel.FieldEvent) -> Unit,
    isLoadingSelections: Boolean = false,
    isSaving: Boolean = false,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        TextField(
            value = formState.name,
            label = "Nome do curso",
            isError = formState.nameError != null,
            onValueChange = { onFormChange(CourseViewModel.FieldEvent.NameChanged(it)) },
            errorResource = formState.nameError,
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
                isSelected = formState.durationType == "Horas",
                onClick = { onFormChange(CourseViewModel.FieldEvent.DurationTypeChanged("Horas")) }
            )

            RadioButtonWithText(
                text = "Aulas",
                isSelected = formState.durationType == "Aulas",
                onClick = { onFormChange(CourseViewModel.FieldEvent.DurationTypeChanged("Aulas")) }
            )
        }

        TextField(
            value = formState.duration.toString(),
            label = "Duração Total",
            isError = formState.durationError != null,
            onValueChange = { onFormChange(CourseViewModel.FieldEvent.DurationChanged(it.toLong())) },
            errorResource = formState.durationError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        DropdownTextField(
            label = "Instituição",
            value = formState.institution.name,
            errorResource = formState.institutionError,
            options = institutionsOptions,
            disabled = isLoadingSelections,
            onValueChange = { onFormChange(CourseViewModel.FieldEvent.InstitutionChanged(it)) }
        )

        DropdownTextField(
            label = "Matéria",
            value = formState.matter.name,
            errorResource = formState.matterError,
            options = mattersOptions,
            disabled = isLoadingSelections,
            onValueChange = { onFormChange(CourseViewModel.FieldEvent.MatterChanged(it)) }
        )

        Button(
            text = "Criar",
            onClick = onSave,
            isLoading = isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

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
        institutionsOptions = listOf(Institution("institution id", "institution name", "user id")),
        mattersOptions = listOf(Matter("matter id", "matter name", "user id")),
        formState = CourseViewModel.CourseState(),
        onFormChange = {},
        onSave = {}
    )
}