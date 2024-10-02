package com.eduardosdl.coursestrack.data.repository

import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.util.UiState

interface MatterRepository {
    fun getAllMattersByUser(result: (UiState<List<Matter>>) -> Unit)
    fun createMatter(name: String, result: (UiState<Matter>) -> Unit)
    fun deleteMatter(matterId: String, result: (UiState<String>) -> Unit)
    fun updateMatterName(matter: Matter, newName: String, result: (UiState<String>) -> Unit)
}
