package com.eduardosdl.coursestrack.ui.course

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.adapters.CustomArrayAdapter
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.databinding.FragmentCreateCourseBinding
import com.eduardosdl.coursestrack.ui.dialogs.CreationDialog
import com.eduardosdl.coursestrack.ui.institution.InstitutionViewModel
import com.eduardosdl.coursestrack.ui.matter.MatterViewModel
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCourseFragment : Fragment() {
    private val viewModel: CourseViewModel by viewModels()
    private val matterViewModel: MatterViewModel by viewModels()
    private val institutionViewModel: InstitutionViewModel by viewModels()

    private lateinit var binding: FragmentCreateCourseBinding
    private var selectedInstitution: Institution? = null
    private var selectedMatter: Matter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCourseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.courseMatterInput.dropDownHeight = 600
        binding.courseInstitutionInput.dropDownHeight = 600

        setupInputListeners()
        checkFieldsForEmptyValues()

        binding.createCourseBtn.setOnClickListener {
            val courseName = binding.courseNameInput.text.toString()
            val courseDurationType = getWorkloadSelected()
            val courseDuration = binding.courseDurationInput.text.toString().toLong()

            viewModel.createCourse(
                courseName,
                courseDurationType,
                courseDuration,
                selectedMatter!!,
                selectedInstitution!!
            )
        }

        binding.createMatterBtn.setOnClickListener {
            CreationDialog("Adicionar matéria", "matéria") {
                matterViewModel.createMatter(it)
                matterViewModel.getAllMatters()
            }.show(
                childFragmentManager,
                "createMatterDialog"
            )
        }

        binding.createInstitutionBtn.setOnClickListener {
            CreationDialog("Adicionar instituição", "instituição") {
                institutionViewModel.createInstitution(it)
                institutionViewModel.getAllInstitutions()
            }.show(
                childFragmentManager,
                "createInstitutionDialog"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        matterViewModel.getAllMatters()
        institutionViewModel.getAllInstitutions()
    }

    private fun observer() {
        matterViewModel.matters.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.courseMatterLayout.isEnabled = false
                }

                is UiState.Failure -> {
                    binding.courseMatterLayout.isEnabled = true
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    binding.courseMatterLayout.isEnabled = true
                    val matterArray = state.data.toTypedArray()
                    val matterArrayAdapter = CustomArrayAdapter(
                        requireContext(),
                        R.layout.item_input_list,
                        state.data.map { it.name }.toTypedArray()
                    )
                    binding.courseMatterInput.setAdapter(matterArrayAdapter)

                    binding.courseMatterInput.setOnItemClickListener { _, _, position, _ ->
                        selectedMatter = matterArray[position]
                        checkFieldsForEmptyValues()
                    }
                }
            }
        }

        institutionViewModel.institutions.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.courseInstitutionLayout.isEnabled = false
                    Toast.makeText(context, "carregando", Toast.LENGTH_SHORT).show()
                }

                is UiState.Failure -> {
                    binding.courseInstitutionLayout.isEnabled = false
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    binding.courseInstitutionLayout.isEnabled = true
                    val institutionArray = state.data.toTypedArray()
                    val institutionsArrayAdapter = CustomArrayAdapter(
                        requireContext(),
                        R.layout.item_input_list,
                        state.data.map { it.name }.toTypedArray()
                    )
                    binding.courseInstitutionInput.setAdapter(institutionsArrayAdapter)

                    binding.courseInstitutionInput.setOnItemClickListener { _, _, position, _ ->
                        selectedInstitution = institutionArray[position]
                        checkFieldsForEmptyValues()
                    }
                }
            }
        }

        viewModel.newCourse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.createCourseBtn.text = ""
                    binding.btnProgress.show()
                }

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    Toast.makeText(context, "Curso criado com sucesso", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun getWorkloadSelected(): String {
        var workloadTypeSelected = ""
        val selectedRadioButtonId = binding.courseWorkloadOptions.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            val selectedWorkload =
                view?.findViewById<RadioButton>(selectedRadioButtonId)?.text.toString()
            workloadTypeSelected = selectedWorkload
        }
        return workloadTypeSelected
    }

    private fun setupInputListeners() {
        binding.courseNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForEmptyValues()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.courseDurationInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForEmptyValues()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.courseWorkloadOptions.setOnCheckedChangeListener { _, _ ->
            checkFieldsForEmptyValues()
        }
    }

    private fun checkFieldsForEmptyValues() {
        val courseName = binding.courseNameInput.text.toString()
        val courseDuration = binding.courseDurationInput.text.toString()
        val isMatterSelected = selectedMatter != null
        val isInstitutionSelected = selectedInstitution != null
        val isWorkloadSelected = binding.courseWorkloadOptions.checkedRadioButtonId != -1

        binding.createCourseBtn.isEnabled = courseName.isNotEmpty() &&
                courseDuration.isNotEmpty() &&
                isMatterSelected &&
                isInstitutionSelected &&
                isWorkloadSelected
    }
}