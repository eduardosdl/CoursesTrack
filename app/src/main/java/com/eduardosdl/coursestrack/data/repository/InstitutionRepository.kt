package com.eduardosdl.coursestrack.data.repository

import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.util.UiState

interface InstitutionRepository {
    fun getAllInstitutionsByUser(
        onSuccess: (List<Institution>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun createInstitution(name: String, result: (UiState<Institution>) -> Unit)
    fun deleteInstitution(institutionId: String, result: (UiState<String>) -> Unit)
    fun updateInstitutionName(
        institution: Institution,
        newName: String,
        result: (UiState<String>) -> Unit
    )

    fun deleteAllInstitutions(result: (UiState<String>) -> Unit)
}