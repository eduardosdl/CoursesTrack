package com.eduardosdl.coursestrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.databinding.ItemSimpleCardBinding

class InstitutionAdapter(
    private val onEditButtonClicked: (Institution) -> Unit,
    private val onDeleteButtonClicked: (Institution) -> Unit
) : RecyclerView.Adapter<InstitutionViewHolder>() {
    private var institutions = mutableListOf<Institution>()

    fun setInstitutionsList(institutions: List<Institution>) {
        this.institutions = institutions.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSimpleCardBinding.inflate(inflater, parent, false)
        return InstitutionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstitutionViewHolder, position: Int) {
        val institution = institutions[position]
        holder.bind(institution, onEditButtonClicked, onDeleteButtonClicked)
    }

    override fun getItemCount(): Int {
        return institutions.size
    }
}

class InstitutionViewHolder(val binding: ItemSimpleCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        institution: Institution,
        onEditButtonClicked: (Institution) -> Unit,
        onDeleteButtonClicked: (Institution) -> Unit
    ) {
        binding.productName.text = institution.name

        binding.editMatterBtn.setOnClickListener {
            onEditButtonClicked(institution)
        }

        binding.deleteMatterBtn.setOnClickListener {
            onDeleteButtonClicked(institution)
        }
    }
}


