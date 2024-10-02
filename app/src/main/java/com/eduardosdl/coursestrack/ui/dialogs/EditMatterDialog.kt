package com.eduardosdl.coursestrack.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.eduardosdl.coursestrack.R

class EditMatterDialog(private val currentName: String, private val onUpdate: (String) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_edit_content, null)

        val nameInput = view.findViewById<EditText>(R.id.name_input)
        nameInput.setText(currentName)

        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dismiss()
        }

        val submitButton = view.findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val newName = nameInput.text.toString().trim()
            if (newName.isNotEmpty()) {
                onUpdate(newName)
                dismiss()
            } else {
                nameInput.error = "Nome da matéria não pode estar vazio"
            }
        }

        return Dialog(requireContext()).apply {
            setContentView(view)
        }
    }
}
