package com.example.android4_1.ui.home.view_pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android4_1.App
import com.example.android4_1.databinding.FragmentAllNotesBinding
import com.example.android4_1.ui.note.Note
import com.example.android4_1.ui.note.NoteItemAdapter
import com.example.android4_1.ui.note.NotesViewModel
import java.time.LocalDate

class AllNotesFragment : Fragment(), OnNoteItemClick {
    private var _binding: FragmentAllNotesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel
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
        val activity = requireActivity()
        notesViewModel =
            ViewModelProvider(activity).get(NotesViewModel::class.java)
        initListiners()
        setRecyclerView()
    }

    private fun setRecyclerView() {

        notesViewModel.notesList.observe(viewLifecycleOwner) {
            binding.rvNotes.apply {
                layoutManager = LinearLayoutManager(context)
                val list = (requireContext().applicationContext as App).mySharedPreferense?.getSavedNotes()
                if(list == null){
                    adapter = it?.let { it1 -> NoteItemAdapter(it1, this@AllNotesFragment) }
                } else {
                    adapter = NoteItemAdapter(list, this@AllNotesFragment)
                }

            }
        }
    }

    private fun initListiners() {
        binding.btnAddNote.setOnClickListener() {
            notesViewModel.addNoteItem(Note("", "", LocalDate.now(), false, false))
            notesViewModel.notesList.observe(viewLifecycleOwner) {
                if (it != null) {
                    (context?.applicationContext as App).mySharedPreferense?.saveNotes(it)
                    binding.rvNotes.layoutManager?.scrollToPosition((it.size) - 1)
                }
            }

        }

    }

    override fun onItemClick(item: Note) {
        notesViewModel.changeNoteItemStatus(item.id)
        notesViewModel.notesList.observe(viewLifecycleOwner) {
            if (it != null) {
                (requireContext().applicationContext as App).mySharedPreferense?.saveNotes(it)
            }
        }
    }
}