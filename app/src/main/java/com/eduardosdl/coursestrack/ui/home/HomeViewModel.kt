package com.eduardosdl.coursestrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _coursesList = MutableLiveData<UiState<List<Course>>>()
    val coursesList: LiveData<UiState<List<Course>>>
        get() = _coursesList

    private val _updateProgress = MutableLiveData<UiState<String>>()
    val updateProgress: LiveData<UiState<String>>
        get() = _updateProgress

    private var isAscending = true

    fun getAllCourses() {
        _coursesList.value = UiState.Loading
        courseRepository.getAllCourses() {
            _coursesList.value = it
        }
    }

    fun updateProgress(course: Course, progress: Long) {
        _updateProgress.value = UiState.Loading
        courseRepository.updateCourseProgress(course, progress) {
            _updateProgress.value = it
        }
    }

    fun orderByCourseName() {
        val currentList = _coursesList.value
        if (currentList is UiState.Success) {
            val sortedList = if (isAscending) {
                currentList.data.sortedBy { it.name }
            } else {
                currentList.data.sortedByDescending { it.name }
            }
            isAscending = !isAscending
            _coursesList.value = UiState.Success(sortedList)
        }
    }
}