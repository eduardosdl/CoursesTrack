package com.eduardosdl.coursestrack.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.eduardosdl.coursestrack.databinding.DialogDeleteBinding

class DeleteDialog(
    private val content: String,
    private val deleteFunction: () -> Unit
) : DialogFragment() {
    lateinit var binding: DialogDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        binding.submitButton.setOnClickListener {
            deleteFunction.invoke()
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.text = content

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }
}