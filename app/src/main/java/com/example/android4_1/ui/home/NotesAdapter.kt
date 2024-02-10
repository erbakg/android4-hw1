package com.example.android4_1.ui.home

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android4_1.R
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.NoteTypes
import com.example.android4_1.databinding.ItemNoteBinding

class NotesAdapter(
    val noteUpdated: (Note) -> Unit,
) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(
    object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    inner class NotesViewHolder(
        private val binding: ItemNoteBinding,
    ) : ViewHolder(binding.root) {
        fun onBind(note: Note, position: Int) {
            binding.tvNumber.text = (position + 1).toString()
            binding.tvTitle.text = note.title
            binding.tvDesc.text = note.description
            if (note.type == NoteTypes.TO_DO) {
                binding.btnToProgress.setImageResource(R.drawable.ic_start_24)
            } else if (note.type == NoteTypes.IN_PROGRESS) {
                binding.btnToProgress.setImageResource(R.drawable.ic_done_all_24)
            } else {
                binding.btnToProgress.visibility = View.GONE
                binding.tvTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                binding.tvDesc.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            }
            binding.btnToProgress.setOnClickListener {
                Log.d("haha", "onBind: ${note}")
                if (note.type == NoteTypes.TO_DO) {
                    noteUpdated(note.copy(type = NoteTypes.IN_PROGRESS))
                } else if (note.type == NoteTypes.IN_PROGRESS) {
                    noteUpdated(note.copy(type = NoteTypes.DONE))
                }

            }
        }
    }
}