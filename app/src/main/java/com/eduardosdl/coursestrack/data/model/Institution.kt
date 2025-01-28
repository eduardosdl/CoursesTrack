package com.eduardosdl.coursestrack.data.model

data class Institution(
    override val id: String? = null,
    override val name: String = "",
    override val userId: String = ""
): Category()
