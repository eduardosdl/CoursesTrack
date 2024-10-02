package com.eduardosdl.coursestrack.data.repository

import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.util.UiState

interface InstitutionRepository {
    fun getAllInstitutionsByUser(result: (UiState<List<Institution>>) -> Unit)
    fun createInstitution(name: String, result: (UiState<Institution>) -> Unit)
    fun deleteInstitution(institutionId: String, result: (UiState<String>) -> Unit)
    fun updateInstitutionName(institution: Institution, newName: String, result: (UiState<String>) -> Unit)

}