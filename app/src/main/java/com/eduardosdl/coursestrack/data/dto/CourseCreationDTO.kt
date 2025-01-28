package com.eduardosdl.coursestrack.data.dto

import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter

data class CourseCreationDTO(
    val name: String,
    val duration: String,
    val durationType: String,
    val institution: Institution,
    val matter: Matter
)
