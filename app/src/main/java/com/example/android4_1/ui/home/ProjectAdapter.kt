package com.example.android4_1.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.android4_1.R
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.ProjectAndNotes
import com.example.android4_1.databinding.ItemProjectGroupBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ProjectAdapter(
    private val itemUpdated: (Note) -> Unit
) : ListAdapter<ProjectAndNotes, ProjectAdapter.ProjectViewHolder>(
    object : DiffUtil.ItemCallback<ProjectAndNotes>() {
        override fun areItemsTheSame(
            oldItem: ProjectAndNotes,
            newItem: ProjectAndNotes
        ) = oldItem.project.projectId == newItem.project.projectId


        override fun areContentsTheSame(
            oldItem: ProjectAndNotes,
            newItem: ProjectAndNotes
        ) = oldItem.noteList == newItem.noteList
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        binding.rvNotes.adapter = NotesAdapter(itemUpdated)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.onBind(getItem(position), holder)
    }

    class ProjectViewHolder(
        private val binding: ItemProjectGroupBinding
    ) : ViewHolder(binding.root) {
        private fun dateIsGone(d1: String): Boolean {
            val firstDate: LocalDateTime = LocalDateTime.parse(d1)
            val secondDate: LocalDateTime = LocalDateTime.now()

            var result = when {
                firstDate.isAfter(secondDate) -> {
                    false
                }

                firstDate.isBefore(secondDate) -> {
                    true
                }

                firstDate == secondDate -> {
                    false
                }

                else -> {
                    false
                }
            }
            return result
        }

        fun onBind(project: ProjectAndNotes, holder: ProjectViewHolder) {
            binding.run {
                holder.itemView.setOnClickListener {
                    if (binding.rvNotes.visibility == View.GONE) {
                        binding.rvNotes.visibility = View.VISIBLE
                        binding.ivArrow.rotationX = 0f
                        binding.rvNotes.background =
                            holder.itemView.context.getDrawable(R.drawable.project_rv_bg)
                    } else {
                        binding.rvNotes.visibility = View.GONE
                        binding.ivArrow.rotationX = 180f
                        binding.rvNotes.background =
                            holder.itemView.context.getDrawable(R.color.white)
                    }
                }
                val formatter = DateTimeFormatter.ofPattern("yyyy MM dd");

                tvProjectName.text = project.project.projectName
                tvProjectDate.text =
                    LocalDateTime.parse(project.project.projectDate).format(formatter).toString()
                val dateIsGone = dateIsGone(d1 = project.project.projectDate)
                if (dateIsGone) {
                    tvProjectDate.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red
                        )
                    )
                } else {
                    tvProjectDate.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        )
                    )
                }
                Glide.with(holder.itemView.context).load(project.project.projectImg).circleCrop()
                    .into(binding.ivProjectAvatar);
                val adapter = binding.rvNotes.adapter as NotesAdapter
                adapter.submitList(project.noteList)
            }
        }
    }
}