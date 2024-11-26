package com.eduardosdl.coursestrack.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.ui.theme.CoursesTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CoursesTrackTheme {
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = {
                            findNavController().navigate(R.id.action_registerFragment_to_home_navigation)
                        },
                        onNavigateToLogin = {
                            findNavController().navigateUp()
                        }
                    )
                }
            }
        }
    }
}