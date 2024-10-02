package com.eduardosdl.coursestrack.ui.matter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eduardosdl.coursestrack.adapters.MatterAdapter
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.databinding.FragmentMatterBinding
import com.eduardosdl.coursestrack.ui.dialogs.CreationDialog
import com.eduardosdl.coursestrack.ui.dialogs.DeleteDialog
import com.eduardosdl.coursestrack.ui.dialogs.EditMatterDialog
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatterFragment : Fragment() {
    private lateinit var binding: FragmentMatterBinding
    private val viewModel: MatterViewModel by viewModels()
    private val matterAdapter = MatterAdapter(
        onEditButtonClicked = { matter ->
            showEditDialog(matter)
        },
        onDeleteButtonClicked = { matter ->
            DeleteDialog(matter.name) {
                viewModel.deleteMatter(matter.id!!)
            }.show(parentFragmentManager, "deleteDialog")
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createMatterBtn.setOnClickListener {
            CreationDialog("Adicionar matéria", "matéria") { matterName ->
                viewModel.createMatter(matterName)
            }.show(childFragmentManager, "createMatterDialog")
        }

        binding.recyclerview.adapter = matterAdapter

        viewModel.getAllMatters()

        observeViewModel()
    }

    private fun showEditDialog(matter: Matter) {
        EditMatterDialog(matter.name ?: "") { newName ->
            viewModel.updateMatterName(matter, newName)
        }.show(childFragmentManager, "editMatterDialog")
    }

    private fun observeViewModel() {
        viewModel.matters.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    matterAdapter.setMattersList(state.data)
                }
            }
        }

        viewModel.newMatter.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    viewModel.getAllMatters()
                }
            }
        }

        viewModel.updateMatter.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Nome da matéria atualizado", Toast.LENGTH_SHORT).show()
                    viewModel.getAllMatters()
                }
            }
        }

        viewModel.deleteMatter.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    Toast.makeText(context, "Matéria excluída com sucesso", Toast.LENGTH_SHORT).show()
                    viewModel.getAllMatters()
                }
            }
        }
    }
}
