package com.eduardosdl.coursestrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.databinding.ItemSimpleCardBinding

class MatterAdapter(
    private val onEditButtonClicked: (Matter) -> Unit,
    private val onDeleteButtonClicked: (Matter) -> Unit
) : RecyclerView.Adapter<MatterViewHolder>() {
    private var matters = mutableListOf<Matter>()

    fun setMattersList(matters: List<Matter>) {
        this.matters = matters.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSimpleCardBinding.inflate(inflater, parent, false)
        return MatterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatterViewHolder, position: Int) {
        val matter = matters[position]
        holder.bind(matter, onEditButtonClicked, onDeleteButtonClicked)
    }

    override fun getItemCount(): Int {
        return matters.size
    }
}

class MatterViewHolder(val binding: ItemSimpleCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        matter: Matter,
        onEditButtonClicked: (Matter) -> Unit,
        onDeleteButtonClicked: (Matter) -> Unit
    ) {
        binding.productName.text = matter.name

        binding.editMatterBtn.setOnClickListener {
            onEditButtonClicked(matter)
        }

        binding.deleteMatterBtn.setOnClickListener {
            onDeleteButtonClicked(matter)
        }
    }
}
