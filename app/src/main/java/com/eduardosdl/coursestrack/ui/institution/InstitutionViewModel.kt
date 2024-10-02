package com.eduardosdl.coursestrack.ui.institution

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.repository.InstitutionRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InstitutionViewModel @Inject constructor(
    private val institutionRepository: InstitutionRepository
) : ViewModel() {

    private val _institutions = MutableLiveData<UiState<List<Institution>>>()
    val institutions: LiveData<UiState<List<Institution>>>
        get() = _institutions

    private val _newInstitution = MutableLiveData<UiState<Institution>>()
    val newInstitution: LiveData<UiState<Institution>>
        get() = _newInstitution

    private val _deleteInstitution = MutableLiveData<UiState<String>>()
    val deleteInstitution: LiveData<UiState<String>> get() = _deleteInstitution

    private val _updateInstitution = MutableLiveData<UiState<String>>()
    val updateInstitution: LiveData<UiState<String>> get() = _updateInstitution

    fun getAllInstitutions() {
        _institutions.value = UiState.Loading
        institutionRepository.getAllInstitutionsByUser { result ->
            _institutions.value = result
        }
    }

    fun createInstitution(name: String) {
        _newInstitution.value = UiState.Loading
        institutionRepository.createInstitution(name) { result ->
            _newInstitution.value = result
            getAllInstitutions()
        }
    }

    fun deleteInstitution(institutionId: String) {
        _deleteInstitution.value = UiState.Loading
        institutionRepository.deleteInstitution(institutionId) { result ->
            _deleteInstitution.value = result
            getAllInstitutions()
        }
    }

    fun updateInstitutionName(institution: Institution, newName: String) {
        _updateInstitution.value = UiState.Loading
        institutionRepository.updateInstitutionName(institution, newName) { result ->
            _updateInstitution.value = result
            getAllInstitutions()
        }
    }
}
