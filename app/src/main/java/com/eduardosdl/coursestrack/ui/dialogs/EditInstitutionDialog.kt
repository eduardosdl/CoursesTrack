package com.eduardosdl.coursestrack.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.eduardosdl.coursestrack.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditInstitutionDialog(
    private val currentName: String,
    private val onNameUpdated: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_edit_institution, null)

        val editTextName = view.findViewById<EditText>(R.id.name_input)
        editTextName.setText(currentName)

        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val submitButton = view.findViewById<Button>(R.id.submitButton)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .create()

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        submitButton.setOnClickListener {
            val newName = editTextName.text.toString()
            if (newName.isNotBlank()) {
                onNameUpdated(newName)
                dialog.dismiss()
            } else {
                editTextName.error = "Nome não pode ser vazio"
            }
        }

        return dialog
    }
}
