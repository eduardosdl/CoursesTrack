package com.eduardosdl.coursestrack.ui.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
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