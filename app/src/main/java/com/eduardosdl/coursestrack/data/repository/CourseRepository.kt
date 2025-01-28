package com.eduardosdl.coursestrack.data.repository

import com.eduardosdl.coursestrack.data.dto.CourseCreationDTO
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.util.UiState

interface CourseRepository {
    fun createCourse(
        courseData: CourseCreationDTO,
        result: (UiState<Course>) -> Unit? = {},
        onSuccess: (Course) -> Unit = {},
        onFailure: (String) -> Unit = {}
    )

    fun getAllCourses(result: (UiState<List<Course>>) -> Unit)

    fun updateCourseProgress(
        course: Course,
        additionalProgress: Long,
        result: (UiState<String>) -> Unit
    )

    fun getCourse(courseId: String, result: (UiState<Course>) -> Unit)

    fun deleteCourse(course: Course, result: (UiState<String>) -> Unit)

    fun updateCourse(course: Course, matter: Matter, institution: Institution, result: (UiState<Course>) -> Unit)

    fun deleteAllCourses(result: (UiState<String>) -> Unit)
}