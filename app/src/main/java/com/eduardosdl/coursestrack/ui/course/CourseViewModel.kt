package com.eduardosdl.coursestrack.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.data.repository.InstitutionRepository
import com.eduardosdl.coursestrack.data.repository.MatterRepository
import com.eduardosdl.coursestrack.util.UiState
import com.eduardosdl.coursestrack.util.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val institutionRepository: InstitutionRepository,
    private val matterRepository: MatterRepository
) :
    ViewModel() {
    private val _newCourse = MutableLiveData<UiState<Course>>()
    val newCourse: LiveData<UiState<Course>>
        get() = _newCourse

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

    fun getAllInstitutions() {
        _institutions.value = ViewModelState.Loading
        institutionRepository.getAllInstitutionsByUser(
            onSuccess = {
                _institutions.value = ViewModelState.Success(it)
            },
            onFailure = {
                _institutions.value = ViewModelState.Failure(it)
            }
        )
    }

    fun getAllMatters() {
        _matters.value = ViewModelState.Loading
        matterRepository.getAllMattersByUser(
            onSuccess = {
                _matters.value = ViewModelState.Success(it)
            },
            onFailure = {
                _matters.value = ViewModelState.Failure(it)
            }
        )
    }

    fun createCourse(
        name: String,
        durationType: String,
        duration: Long,
        matter: Matter,
        institution: Institution
    ) {
        val course = Course(name = name, durationType = durationType, duration = duration)
        _newCourse.value = UiState.Loading
        courseRepository.createCourse(course, institution, matter) {
            _newCourse.value = it
        }
    }

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
}