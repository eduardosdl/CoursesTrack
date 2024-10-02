package com.eduardosdl.coursestrack.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.databinding.FragmentRegisterBinding
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.loginLabel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.registerBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (validate(email, password, confirmPassword)) {
                viewModel.register(email, password)
            }
        }
    }

    private fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.registerBtn.text = ""
                    binding.btnProgress.show()
                }

                is UiState.Failure -> {
                    binding.btnProgress.hide()
                    binding.registerBtn.text = "Criar conta"
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    binding.btnProgress.hide()
                    binding.registerBtn.text = "Criar conta"
                    findNavController().navigate(R.id.action_registerFragment_to_home_navigation)
                }
            }
        }
    }

    private fun validate(email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true
        if (email.isNullOrEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.emailLayout.error = "Informe um email"
            isValid = false
        }

        if (password.isNullOrEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.passwordLayout.error = "Informe uma senha"
            isValid = false
        }

        if (confirmPassword.isNullOrEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            binding.confirmPasswordLayout.error = "Informe uma senha"
            isValid = false
        } else if (password != confirmPassword) {
            Toast.makeText(context, "As senhas estão diferentes", Toast.LENGTH_SHORT)
                .show()
            binding.passwordLayout.error = "Senhas diferentes"
            binding.confirmPasswordLayout.error = "Senhas diferentes"
            isValid = false
        }
        return isValid
    }
}