package com.eduardosdl.coursestrack.ui.course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.util.ViewModelState

@Composable
fun CreateCourseRoute(
    viewModel: CourseViewModel,
) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.getAllInstitutions()
        viewModel.getAllMatters()
    }

    val institutionsState by viewModel.institutions.collectAsState()
    val mattersState by viewModel.matters.collectAsState()
    val newCourseState by viewModel.newCourse.collectAsState()
    val formState by viewModel.formState.collectAsState()

    val isLoading = institutionsState is ViewModelState.Loading
    val isSaving = newCourseState is ViewModelState.Loading

    val institutions = (institutionsState as? ViewModelState.Success)?.data ?: emptyList()
    val matters = (mattersState as? ViewModelState.Success)?.data ?: emptyList()

    CreateCourseContent(
        institutions = institutions,
        matters = matters,
        formState = formState,
        onFormChange = viewModel::onFormChange,
        isLoadingSelections = isLoading,
        isSaving = isSaving,

    ) {
        viewModel.createCourse()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseContent(
    institutions: List<Institution>,
    matters: List<Matter>,
    formState: CourseViewModel.CourseState,
    onFormChange: (CourseViewModel.FieldEvent) -> Unit,
    isLoadingSelections: Boolean = false,
    isSaving: Boolean = false,
    onSave: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Criar curso")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CourseForm(
                institutionsOptions = institutions,
                mattersOptions = matters,
                formState = formState,
                onFormChange = onFormChange,
                isLoadingSelections = isLoadingSelections,
                isSaving = isSaving,
                onSave = onSave
            )
        }
    }
}