package com.eduardosdl.coursestrack.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.eduardosdl.coursestrack.databinding.DialogCreationBinding

class CreationDialog(
    private val title: String,
    private val inputHint: String,
    private val creationFunction: (name: String) -> Unit
) : DialogFragment() {
    lateinit var binding: DialogCreationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.submitButton.setOnClickListener {
            val name = binding.nameInput.text.toString()

            if (name.isNullOrEmpty()) {
                binding.nameLayout.error = "$title é obrigatório"
            } else {
                creationFunction.invoke(name)
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = title
        binding.nameLayout.hint = inputHint

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}