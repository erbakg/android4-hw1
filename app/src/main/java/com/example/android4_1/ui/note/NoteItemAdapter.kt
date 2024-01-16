package com.example.android4_1.ui.note

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android4_1.R
import com.example.android4_1.databinding.ItemNoteBinding

class NoteItemAdapter(
    private val notesList: List<Note>
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val from = LayoutInflater.from(parent.context)
        val binding =ItemNoteBinding.inflate(from, parent, false)
        return ViewHolder(parent.context, binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = notesList[position]
        holder.bind(noteItem, position)
        holder.itemView.setOnClickListener{
            NoteFragment.navigateToNote(
                id = noteItem.id.toString(),
                title = noteItem.title,
                description = noteItem.description,
                date = noteItem.date.toString(),
                navController = findNavController(holder.itemView),
                destination = R.id.action_navigation_home_to_noteFragment
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}

class ViewHolder(private val context: Context, private val binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {
fun bind (noteItem: Note, position: Int){
    binding.tvNumber.text = (position+1).toString()
    binding.tvTitle.setText(noteItem.title)
    binding.tvDesc.setText(noteItem.description)
}
}