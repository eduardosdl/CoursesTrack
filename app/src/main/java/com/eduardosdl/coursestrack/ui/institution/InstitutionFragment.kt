package com.eduardosdl.coursestrack.ui.institution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eduardosdl.coursestrack.adapters.InstitutionAdapter
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.databinding.DialogDeleteBinding
import com.eduardosdl.coursestrack.databinding.FragmentInstitutionBinding
import com.eduardosdl.coursestrack.ui.dialogs.CreationDialog
import com.eduardosdl.coursestrack.ui.dialogs.EditInstitutionDialog
import com.eduardosdl.coursestrack.util.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstitutionFragment : Fragment() {
    private lateinit var binding: FragmentInstitutionBinding
    private val viewModel: InstitutionViewModel by viewModels()
    private val institutionAdapter = InstitutionAdapter(
        onEditButtonClicked = { institution ->
            showEditDialog(institution)
        },
        onDeleteButtonClicked = { institution ->
            showDeleteDialog(institution)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstitutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createInstitutionBtn.setOnClickListener {
            CreationDialog("Adicionar instituição", "instituição") { institutionName ->
                viewModel.createInstitution(institutionName)
            }.show(childFragmentManager, "createInstitutionDialog")
        }

        binding.recyclerview.adapter = institutionAdapter

        viewModel.getAllInstitutions()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.institutions.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Exibir um indicador de loading
                }
                is UiState.Failure -> {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    institutionAdapter.setInstitutionsList(state.data)
                }
            }
        }

        viewModel.deleteInstitution.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Exibir um indicador de loading
                }
                is UiState.Failure -> {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
                    viewModel.getAllInstitutions()
                }
            }
        }

        viewModel.updateInstitution.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Exibir um indicador de loading
                }
                is UiState.Failure -> {
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
                    viewModel.getAllInstitutions()
                }
            }
        }
    }

    private fun showEditDialog(institution: Institution) {
        EditInstitutionDialog(institution.name ?: "") { newName ->
            viewModel.updateInstitutionName(institution, newName)
        }.show(childFragmentManager, "editInstitutionDialog")
    }

    private fun showDeleteDialog(institution: Institution) {
        val dialogBinding = DialogDeleteBinding.inflate(layoutInflater)

        dialogBinding.content.text = institution.name ?: "Sem nome"
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.submitButton.setOnClickListener {
            val institutionId = institution.id
            if (institutionId != null) {
                viewModel.deleteInstitution(institutionId)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "ID da instituição não encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
