package com.eduardosdl.coursestrack.ui.matter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.data.repository.MatterRepository
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatterViewModel @Inject constructor(
    private val matterRepository: MatterRepository
) : ViewModel() {

    private val _matters = MutableLiveData<UiState<List<Matter>>>()
    val matters: LiveData<UiState<List<Matter>>>
        get() = _matters

    private val _newMatter = MutableLiveData<UiState<Matter>>()
    val newMatter: LiveData<UiState<Matter>>
        get() = _newMatter

    private val _deleteMatter = MutableLiveData<UiState<String>>()
    val deleteMatter: LiveData<UiState<String>>
        get() = _deleteMatter

    private val _updateMatter = MutableLiveData<UiState<String>>()
    val updateMatter: LiveData<UiState<String>>
        get() = _updateMatter

    fun getAllMatters() {
        _matters.value = UiState.Loading
        matterRepository.getAllMattersByUser { result ->
            _matters.value = result
        }
    }

    fun createMatter(name: String) {
        _newMatter.value = UiState.Loading
        matterRepository.createMatter(name) { result ->
            _newMatter.value = result
        }
    }

    fun deleteMatter(matterId: String) {
        _deleteMatter.value = UiState.Loading
        matterRepository.deleteMatter(matterId) { result ->
            _deleteMatter.value = result
        }
    }

    fun updateMatterName(matter: Matter, newName: String) {
        _updateMatter.value = UiState.Loading
        matterRepository.updateMatterName(matter, newName) { result ->
            _updateMatter.value = result
        }
    }
}
