package com.example.android4_1.ui.home.view_pager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android4_1.data.NoteManager
import com.example.android4_1.databinding.FragmentAllNotesBinding
import com.example.android4_1.models.Note
import com.example.android4_1.ui.note.NoteItemAdapter
import java.time.LocalDate
import java.util.UUID

class AllNotesFragment : Fragment(), OnNoteItemClick {
    private var _binding: FragmentAllNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListiners()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        NoteManager.dao.getNotes().observe(viewLifecycleOwner) { notes ->
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = NoteItemAdapter(notes, this@AllNotesFragment)

            }
        }
    }

    private fun initListiners() {
        binding.btnAddNote.setOnClickListener() {
            val noteDao = NoteManager.dao
            noteDao.addNoteItem(Note("", "", LocalDate.now().toString(), false, false))
            NoteManager.dao.getNotes().observe(viewLifecycleOwner) { notes ->
                binding.rvNotes.layoutManager?.scrollToPosition((notes.size) - 1)
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