package com.eduardosdl.coursestrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.adapters.CourseAdapter
import com.eduardosdl.coursestrack.databinding.FragmentHomeBinding
import com.eduardosdl.coursestrack.ui.dialogs.UpdateProgressDialog
import com.eduardosdl.coursestrack.ui.shared.SharedViewModel
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val courseAdapter = CourseAdapter(
        onProgressButtonClicked = { course ->
            UpdateProgressDialog(course) {
                viewModel.updateProgress(course, it)
            }.show(parentFragmentManager, "progressDialog")
        },
        onDetailsButtonClicked = { course ->
            val bundle = Bundle()
            bundle.putParcelable("courseData", course)
            findNavController().navigate(R.id.action_homeFragment_to_detailsCourseFragment, bundle)
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        sharedViewModel.getUserId { id ->
            if (id == null) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }

        binding.coursesRecycleView.adapter = courseAdapter

        binding.createCourseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createCourseFragment)
        }

        binding.orderCourseBtn.setOnClickListener {
            viewModel.orderByCourseName()
        }
    }

    override fun onResume() {
        super.onResume()

            viewModel.getAllCourses()

    }

    private fun observer() {
        viewModel.coursesList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loading.root.visibility = View.VISIBLE
                }

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    binding.loading.root.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.loading.root.visibility = View.GONE
                    courseAdapter.setCoursesList(state.data)
                }
            }
        }

        viewModel.updateProgress.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loading.root.visibility = View.VISIBLE
                }

                is UiState.Failure -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    binding.loading.root.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.loading.root.visibility = View.GONE
                    viewModel.getAllCourses()
                }
            }
        }
    }
}