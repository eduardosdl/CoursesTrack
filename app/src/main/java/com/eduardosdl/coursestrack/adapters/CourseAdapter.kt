package com.eduardosdl.coursestrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import calculateProgressPercentage
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.databinding.ItemCourseCardBinding

class CourseAdapter(
    private val onProgressButtonClicked: (Course) -> Unit,
    private val onDetailsButtonClicked: (Course) -> Unit) :
    RecyclerView.Adapter<CourseViewHolder>() {
    private var courses = mutableListOf<Course>()

    fun setCoursesList(courses: List<Course>) {
        this.courses = courses.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseCardBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course, onProgressButtonClicked, onDetailsButtonClicked)
    }

    override fun getItemCount(): Int {
        return courses.size
    }
}

class CourseViewHolder(val binding: ItemCourseCardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        course: Course,
        onProgressButtonClicked: (Course) -> Unit,
        onDetailsButtonClicked: (Course) -> Unit
    ) {
        binding.title.text = course.name
        binding.matter.text = course.matterName
        binding.institution.text = course.institutionName
        binding.progressIndicator.progress = calculateProgressPercentage(course.duration, course.progress)

        binding.updateProgressBtn.setOnClickListener {
            onProgressButtonClicked(course)
        }

        binding.courseDetailsBtn.setOnClickListener {
            onDetailsButtonClicked(course)
        }
    }
}

