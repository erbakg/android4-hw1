package com.example.android4_1.ui.home.view_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android4_1.App
import com.example.android4_1.data.NoteManager
import com.example.android4_1.databinding.FragmentInProgressNotesBinding
import com.example.android4_1.models.Note
import com.example.android4_1.ui.note.NoteItemAdapter
import java.time.LocalDate

class InProgressNotesFragment : Fragment(), OnNoteItemClick {

    private var _binding: FragmentInProgressNotesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInProgressNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        NoteManager.dao.getNotes().observe(viewLifecycleOwner) { notes ->
            val filteredNotes = notes.filter { note: Note -> note.inProgress }
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = NoteItemAdapter(filteredNotes, this@InProgressNotesFragment)

            }
        }
    }

    override fun onItemClick(item: Note) {
        if (!item.done && !item.inProgress) {
            NoteManager.dao.updateNoteItem(
                Note(
                    title = item.title,
                    id = item.id,
                    description = item.description,
                    date = LocalDate.now().toString(),
                    done = false,
                    inProgress = true
                )
            )
        } else if(item.inProgress) {
            NoteManager.dao.updateNoteItem(
                Note(
                    title = item.title,
                    description = item.description,
                    id = item.id,
                    date = LocalDate.now().toString(),
                    done = true,
                    inProgress = false,
                )
            )
        }
    }
}