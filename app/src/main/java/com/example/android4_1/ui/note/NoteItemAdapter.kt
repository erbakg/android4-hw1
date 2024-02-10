package com.example.android4_1.ui.note

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android4_1.R
import com.example.android4_1.databinding.ItemNoteBinding
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.NoteTypes
import com.example.android4_1.ui.notes.view_pager.OnNoteItemClick


class NoteItemAdapter(
    private val notesList: List<Note>,
    private val onNoteItemClick: OnNoteItemClick
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(from, parent, false)
        return ViewHolder(parent.context, binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = notesList[position]
        holder.bind(noteItem, position, onNoteItemClick)
        if (notesList[position].type == NoteTypes.IN_PROGRESS) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.teal
                )
            )
        }
        if (position == notesList.size - 1) {
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 200
            holder.itemView.layoutParams = params
        }
        holder.itemView.setOnClickListener {
            NoteFragment.navigateToNote(
                id = noteItem.id.toString(),
                title = noteItem.title,
                description = noteItem.description,
                projectId = noteItem.projectId.toString(),
                navController = findNavController(holder.itemView),
                destination = R.id.action_navigation_notes_list_to_noteFragment
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}

class ViewHolder(
    private val context: Context,
    private val binding: ItemNoteBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(noteItem: Note, position: Int, onNoteItemClick: OnNoteItemClick) {
        binding.tvNumber.text = (position + 1).toString()
        binding.tvTitle.text = noteItem.title
        binding.tvDesc.text = noteItem.description

        if (noteItem.type == NoteTypes.TO_DO) {
            binding.btnToProgress.setImageResource(R.drawable.ic_start_24)
        } else if (noteItem.type == NoteTypes.IN_PROGRESS) {
            binding.btnToProgress.setImageResource(R.drawable.ic_done_all_24)
        } else {
            Log.d("haha", "bind: "+ noteItem.title.toString())
            binding.btnToProgress.visibility = View.GONE
            binding.tvTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            binding.tvDesc.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        }
        binding.btnToProgress.setOnClickListener {
            onNoteItemClick.onItemClick(noteItem)
        }
    }
}