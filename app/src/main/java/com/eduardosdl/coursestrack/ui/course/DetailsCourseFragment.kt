package com.eduardosdl.coursestrack.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import calculateProgressPercentage
import com.eduardosdl.coursestrack.MainActivity
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.databinding.FragmentDetailsCourseBinding
import com.eduardosdl.coursestrack.ui.dialogs.DeleteDialog
import com.eduardosdl.coursestrack.ui.dialogs.UpdateProgressDialog
import com.eduardosdl.coursestrack.ui.shared.SharedViewModel
import com.eduardosdl.coursestrack.util.UiState
import com.eduardosdl.coursestrack.util.limitTitleLength
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsCourseFragment : Fragment() {
    private val args: DetailsCourseFragmentArgs by navArgs()
    private val viewModel: CourseViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FragmentDetailsCourseBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsCourseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.editCourseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_detailsCourseFragment_to_editCourseFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        populateWithCourseData(args.courseData)
        sharedViewModel.getCourse(args.courseData.id!!)
    }

    private fun observer() {
        sharedViewModel.selectedCourse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.updateProgressBtn.isEnabled = false
                    binding.deleteCourseBtn.isEnabled = false
                    binding.editCourseBtn.isEnabled = false
                    binding.loading.root.visibility = View.VISIBLE
                }

                is UiState.Failure -> {
                    binding.updateProgressBtn.isEnabled = false
                    binding.deleteCourseBtn.isEnabled = false
                    binding.editCourseBtn.isEnabled = false
                    binding.loading.root.visibility = View.GONE
                    Toast.makeText(context, "Houve um erro ao buscar o curso", Toast.LENGTH_SHORT)
                        .show()
                }

                is UiState.Success -> {
                    binding.updateProgressBtn.isEnabled = true
                    binding.deleteCourseBtn.isEnabled = true
                    binding.editCourseBtn.isEnabled = true
                    binding.loading.root.visibility = View.GONE
                    if (args.courseData != state.data) {
                        populateWithCourseData(state.data)
                    }
                }
            }
        }

        viewModel.deleteCourse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.updateProgressBtn.isEnabled = false
                    binding.deleteCourseBtn.isEnabled = false
                    binding.editCourseBtn.isEnabled = false
                }

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT)
                        .show()
                    binding.updateProgressBtn.isEnabled = true
                    binding.deleteCourseBtn.isEnabled = true
                    binding.editCourseBtn.isEnabled = true
                }

                is UiState.Success -> {
                    findNavController().popBackStack()
                    Toast.makeText(context, state.data, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun populateWithCourseData(course: Course) {
        (activity as MainActivity).updateToolbarTitle(course.name.limitTitleLength(15))

        binding.durationLabel.text =
            "${course.durationType} totais: ${course.duration}"
        binding.progressLabel.text =
            "${course.durationType} assistidas: ${course.progress}"

        val progressPerCent =
            calculateProgressPercentage(course.duration, course.progress)
        binding.progress.progress = progressPerCent
        binding.progressPerCent.text = "$progressPerCent %"

        binding.matter.text = course.matterName
        binding.institution.text = course.institutionName

        binding.updateProgressBtn.setOnClickListener {
            UpdateProgressDialog(course) {
                viewModel.updateProgress(course, it)
                sharedViewModel.getCourse(course.id!!)
            }.show(parentFragmentManager, "progressDialog")
        }

        binding.deleteCourseBtn.setOnClickListener {
            DeleteDialog(course.name) {
                viewModel.deleteCourse(course)
            }.show(parentFragmentManager, "deleteDialog")
        }
    }
}