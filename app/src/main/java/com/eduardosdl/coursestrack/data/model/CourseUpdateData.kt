package com.eduardosdl.coursestrack.data.model

data class CourseUpdateData(
    val name: String,
    val durationType: String,
    val duration: Long,
    val matter: Matter,
    val institution: Institution
)
