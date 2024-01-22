package com.example.android4_1.ui.home.view_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android4_1.App
import com.example.android4_1.R
import com.example.android4_1.databinding.FragmentAllNotesBinding
import com.example.android4_1.databinding.FragmentInProgressNotesBinding
import com.example.android4_1.ui.note.Note
import com.example.android4_1.ui.note.NoteItemAdapter
import com.example.android4_1.ui.note.NotesViewModel

class InProgressNotesFragment : Fragment(), OnNoteItemClick {

    private var _binding: FragmentInProgressNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel
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
        val activity = requireActivity()
        notesViewModel =
            ViewModelProvider(activity).get(NotesViewModel::class.java)

        setRecyclerView()
    }

    private fun setRecyclerView() {

        notesViewModel.notesList.observe(viewLifecycleOwner) {
            val list =
                (requireContext().applicationContext as App).mySharedPreferense?.getSavedNotes()
            val filteredNotes = if (list != null) {
                list.filter { note: Note -> note.inProgress }
            } else {
                it?.filter { note: Note -> note.inProgress }
            }
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = NoteItemAdapter(filteredNotes!!, this@InProgressNotesFragment)
            }
        }
    }

    override fun onItemClick(item: Note) {
        notesViewModel.changeNoteItemStatus(item.id)
    }
}