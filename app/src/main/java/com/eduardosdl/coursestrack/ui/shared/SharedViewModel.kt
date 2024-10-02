package com.eduardosdl.coursestrack.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _selectedCourse = MutableLiveData<UiState<Course>>()
    val selectedCourse: LiveData<UiState<Course>> get() = _selectedCourse

    fun getCourse(id: String) {
        _selectedCourse.value = UiState.Loading

        courseRepository.getCourse(id) {
            _selectedCourse.value = it
        }
    }

    fun updateCourse(
        course: Course,
        name: String,
        durationType: String,
        duration: Long,
        matter: Matter,
        institution: Institution
    ) {
        val updatedCourse =
            course.copy(name = name, durationType = durationType, duration = duration)
        _selectedCourse.value = UiState.Loading

        courseRepository.updateCourse(updatedCourse, matter, institution) {
            _selectedCourse.value = it
        }
    }

    fun getUserId(result: (userId: String?) -> Unit) {
        authRepository.getSession { userId ->
            if (userId == null) {
                result.invoke("Usuário não autenticado")
            }

            result.invoke(userId)
        }
    }

}