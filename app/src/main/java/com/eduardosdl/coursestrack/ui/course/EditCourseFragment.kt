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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.MainActivity
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.adapters.CustomArrayAdapter
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.databinding.FragmentEditCourseBinding
import com.eduardosdl.coursestrack.ui.dialogs.CreationDialog
import com.eduardosdl.coursestrack.ui.institution.InstitutionViewModel
import com.eduardosdl.coursestrack.ui.matter.MatterViewModel
import com.eduardosdl.coursestrack.ui.shared.SharedViewModel
import com.eduardosdl.coursestrack.util.UiState
import com.eduardosdl.coursestrack.util.limitTitleLength
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCourseFragment : Fragment() {
    private val viewModel: CourseViewModel by viewModels()
    private val matterViewModel: MatterViewModel by viewModels()
    private val institutionViewModel: InstitutionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentEditCourseBinding
    private var selectedInstitution: Institution? = null
    private var selectedMatter: Matter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCourseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.saveCourseBtn.isEnabled = false //

        binding.courseNameInput.addTextChangedListener(textWatcher)
        binding.courseDurationInput.addTextChangedListener(textWatcher)
        binding.courseMatterInput.addTextChangedListener(textWatcher)
        binding.courseInstitutionInput.addTextChangedListener(textWatcher)


        binding.courseWorkloadOptions.setOnCheckedChangeListener { _, _ ->
            validateFields()
        }

        binding.createMatterBtn.setOnClickListener {
            val createMatterDialog = CreationDialog("Adicionar matéria", "matéria") {
                matterViewModel.createMatter(it)
            }
            createMatterDialog.show(
                childFragmentManager,
                "createMatterDialog"
            )
        }

        binding.createInstitutionBtn.setOnClickListener {
            val createInstitutionDialog = CreationDialog("Adicionar instituição", "instituição") {
                institutionViewModel.createInstitution(it)
            }
            createInstitutionDialog.show(
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
                        validateFields()
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
                    binding.courseInstitutionLayout.isEnabled = true
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
                        validateFields()
                    }
                }
            }
        }

        sharedViewModel.selectedCourse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {}

                is UiState.Failure -> {
                    binding.saveCourseBtn.isEnabled = false
                    binding.createMatterBtn.isEnabled = false
                    binding.createInstitutionBtn.isEnabled = false
                    Toast.makeText(context, "Houve um erro ao buscar o curso", Toast.LENGTH_SHORT)
                        .show()
                }

                is UiState.Success -> {
                    binding.saveCourseBtn.isEnabled = true
                    binding.createMatterBtn.isEnabled = true
                    binding.createInstitutionBtn.isEnabled = true
                    populateWithCourseData(state.data)
                }
            }
        }
    }

    private fun populateWithCourseData(course: Course) {
        (activity as MainActivity).updateToolbarTitle(course.name.limitTitleLength(15))

        binding.courseNameInput.setText(course.name)

        when (course.durationType) {
            "Horas" -> binding.courseWorkloadOptions.check(R.id.course_workload_hours)
            "Aulas" -> binding.courseWorkloadOptions.check(R.id.course_workload_classes)
        }

        binding.courseDurationInput.setText(course.duration.toString())

        binding.saveCourseBtn.setOnClickListener {
            val courseName = binding.courseNameInput.text.toString()
            val courseDurationType = getWorkloadSelected()
            val courseDuration = binding.courseDurationInput.text.toString().toLong()

            sharedViewModel.updateCourse(
                course,
                courseName,
                courseDurationType,
                courseDuration,
                selectedMatter!!,
                selectedInstitution!!
            )

            findNavController().popBackStack()
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateFields()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateFields() {
        val isNameFilled = binding.courseNameInput.text.toString().isNotEmpty()
        val isDurationFilled = binding.courseDurationInput.text.toString().isNotEmpty()
        val isMatterSelected = selectedMatter != null
        val isInstitutionSelected = selectedInstitution != null
        val isWorkloadSelected = binding.courseWorkloadOptions.checkedRadioButtonId != -1

        binding.saveCourseBtn.isEnabled =
            isNameFilled && isDurationFilled && isMatterSelected && isInstitutionSelected && isWorkloadSelected
    }
}
