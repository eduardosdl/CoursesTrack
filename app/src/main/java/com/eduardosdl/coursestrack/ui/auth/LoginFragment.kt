package com.eduardosdl.coursestrack.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.databinding.FragmentLoginBinding
import com.eduardosdl.coursestrack.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.createAccountLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.emailInput.addTextChangedListener {
            binding.emailLayout.error = null
        }
        binding.passwordInput.addTextChangedListener {
            binding.passwordLayout.error = null
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (validate(email, password)) {
                viewModel.login(email, password)
            }
        }
    }

    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loginBtn.text = ""
                    binding.btnProgress.show()
                }

                is UiState.Failure -> {
                    binding.btnProgress.hide()
                    binding.loginBtn.text = "Entrar"
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    binding.btnProgress.hide()
                    binding.loginBtn.text = "Entrar"
                    findNavController().navigate(R.id.action_loginFragment_to_home_navigation)
                }
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
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
        return isValid
    }
}