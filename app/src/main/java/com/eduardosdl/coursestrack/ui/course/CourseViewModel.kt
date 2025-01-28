package com.eduardosdl.coursestrack.ui.course

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.data.dto.CourseCreationDTO
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.data.repository.InstitutionRepository
import com.eduardosdl.coursestrack.data.repository.MatterRepository
import com.eduardosdl.coursestrack.util.UiState
import com.eduardosdl.coursestrack.util.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val institutionRepository: InstitutionRepository,
    private val matterRepository: MatterRepository
) :
    ViewModel() {
    private val _updateProgress = MutableLiveData<UiState<String>>()
    val updateProgress: LiveData<UiState<String>>
        get() = _updateProgress

    private val _deleteCourse = MutableLiveData<UiState<String>>()
    val deleteCourse: LiveData<UiState<String>> get() = _deleteCourse

    private val _institutions =
        MutableStateFlow<ViewModelState<List<Institution>>>(ViewModelState.Idle)
    val institutions: StateFlow<ViewModelState<List<Institution>>> = _institutions

    private val _matters = MutableStateFlow<ViewModelState<List<Matter>>>(ViewModelState.Idle)
    val matters: StateFlow<ViewModelState<List<Matter>>> = _matters

    private val _newCourse = MutableStateFlow<ViewModelState<Course>>(ViewModelState.Idle)
    val newCourse: StateFlow<ViewModelState<Course>> = _newCourse

    private val _formState = MutableStateFlow(CourseState())
    val formState: StateFlow<CourseState> = _formState

    fun onFormChange(event: FieldEvent) {
        when (event) {
            is FieldEvent.NameChanged -> {
                _formState.value = _formState.value.copy(name = event.name)
                _formState.value = _formState.value.copy(nameError = null)
            }

            is FieldEvent.DurationTypeChanged -> {
                _formState.value = _formState.value.copy(durationType = event.durationType)
                _formState.value = _formState.value.copy(durationTypeError = null)
            }

            is FieldEvent.DurationChanged -> {
                _formState.value = _formState.value.copy(duration = event.duration)
                _formState.value = _formState.value.copy(durationError = null)
            }

            is FieldEvent.MatterChanged -> {
                _formState.value = _formState.value.copy(matter = event.matter)
                _formState.value = _formState.value.copy(matterError = null)
            }

            is FieldEvent.InstitutionChanged -> {
                _formState.value = _formState.value.copy(institution = event.institution)
                _formState.value = _formState.value.copy(institutionError = null)
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (_formState.value.name.isEmpty()) {
            _formState.value = _formState.value.copy(nameError = R.string.password_confirm)
            isValid = false
        }

        if (_formState.value.duration == 0L) {
            _formState.value = _formState.value.copy(durationError = R.string.password_confirm)
            isValid = false
        }

        if (_formState.value.matter.name.isEmpty()) {
            _formState.value = _formState.value.copy(matterError = R.string.password_confirm)
            isValid = false
        }

        if (_formState.value.institution.name.isEmpty()) {
            _formState.value = _formState.value.copy(institutionError = R.string.password_confirm)
            isValid = false
        }

        return isValid
    }

    fun getAllInstitutions() {
        viewModelScope.launch {
            _institutions.value = ViewModelState.Loading
            delay(2000L)
            institutionRepository.getAllInstitutionsByUser(
                onSuccess = {
                    _institutions.value = ViewModelState.Success(it)
                },
                onFailure = {
                    _institutions.value = ViewModelState.Failure(it)
                }
            )

        }
    }

    fun getAllMatters() {
        viewModelScope.launch {

            _matters.value = ViewModelState.Loading
            delay(2000L)
            matterRepository.getAllMattersByUser(
                onSuccess = {
                    _matters.value = ViewModelState.Success(it)
                },
                onFailure = {
                    _matters.value = ViewModelState.Failure(it)
                }
            )
        }
    }


    fun createCourse() {
        viewModelScope.launch {
            _newCourse.value = ViewModelState.Loading
            val newCourse = CourseCreationDTO(
                name = _formState.value.name,
                duration = _formState.value.duration.toString(),
                durationType = _formState.value.durationType,
                institution = _formState.value.institution,
                matter = _formState.value.matter
            )

            delay(2000L)

            _newCourse.value = ViewModelState.Success(Course())

            if (!validate()) Log.d("test", "Invalid course data")
            else Log.d("test", "Valid course data: $newCourse")

        }

    }
    //        _newCourse.value = ViewModelState.Loading
//        courseRepository.createCourse(
//            courseData,
//            onSuccess = {
//                _newCourse.value = ViewModelState.Success(it)
//            },
//            onFailure = {
//                _newCourse.value = ViewModelState.Failure(it)
//            }
//        )

    fun updateProgress(course: Course, progress: Long) {
        _updateProgress.value = UiState.Loading
        courseRepository.updateCourseProgress(course, progress) {
            _updateProgress.value = it
        }
    }

    fun deleteCourse(course: Course) {
        _deleteCourse.value = UiState.Loading
        courseRepository.deleteCourse(course) {
            _deleteCourse.value = it
        }
    }

    sealed class FieldEvent {
        data class NameChanged(val name: String) : FieldEvent()
        data class DurationTypeChanged(val durationType: String) : FieldEvent()
        data class DurationChanged(val duration: Long) : FieldEvent()
        data class MatterChanged(val matter: Matter) : FieldEvent()
        data class InstitutionChanged(val institution: Institution) : FieldEvent()
    }

    data class CourseState(
        val name: String = "",
        @StringRes val nameError: Int? = null,
        val durationType: String = "Horas",
        @StringRes val durationTypeError: Int? = null,
        val duration: Long = 0,
        @StringRes val durationError: Int? = null,
        val matter: Matter = Matter(),
        @StringRes val matterError: Int? = null,
        val institution: Institution = Institution(),
        @StringRes val institutionError: Int? = null
    )
}