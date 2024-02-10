package com.example.android4_1.ui.notes.view_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android4_1.data.DatabaseManager
import com.example.android4_1.databinding.FragmentDoneNotesBinding
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.NoteTypes
import com.example.android4_1.ui.note.NoteItemAdapter
import java.time.LocalDate

class DoneNotesFragment : Fragment(), OnNoteItemClick {
    private var _binding: FragmentDoneNotesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoneNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        DatabaseManager.noteDao.getNotesByType(NoteTypes.DONE).observe(viewLifecycleOwner) { notes ->
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = NoteItemAdapter(notes, this@DoneNotesFragment)

            }
        }
    }

    override fun onItemClick(item: Note) {
        if (item.type == NoteTypes.TO_DO) {
            DatabaseManager.noteDao.updateNoteItem(
                Note(
                    title = item.title,
                    id = item.id,
                    description = item.description,
                    date = LocalDate.now().toString(),
                    type = NoteTypes.IN_PROGRESS,
                    projectId = item.projectId
                )
            )
        } else if(item.type == NoteTypes.IN_PROGRESS) {
            DatabaseManager.noteDao.updateNoteItem(
                Note(
                    title = item.title,
                    description = item.description,
                    id = item.id,
                    date = LocalDate.now().toString(),
                    type = NoteTypes.DONE,
                    projectId = item.projectId
                )
            )
        }
    }

}