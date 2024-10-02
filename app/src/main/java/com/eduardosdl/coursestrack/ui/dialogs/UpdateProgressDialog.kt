package com.eduardosdl.coursestrack.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.databinding.DialogProgressUpdateBinding

class UpdateProgressDialog(
    private val course: Course,
    private val saveProgress: (progress: Long) -> Unit
) : DialogFragment() {
    lateinit var binding: DialogProgressUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProgressUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val progressInput = binding.progressInput.text.toString()
            if (progressInput.isNotEmpty()) {
                val progress = progressInput.toLong()
                saveProgress.invoke(progress)
                dismiss()
            } else {
                binding.progressInput.error = "Por favor, insira um numero valido"
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

}

