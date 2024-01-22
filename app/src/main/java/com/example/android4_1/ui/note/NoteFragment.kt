package com.example.android4_1.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.android4_1.App
import com.example.android4_1.databinding.FragmentNoteBinding
import java.time.LocalDate
import java.util.UUID

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        notesViewModel = ViewModelProvider(activity).get(NotesViewModel::class.java)
        val id = arguments?.getString(ID_KEY)
        val date = arguments?.getString(DATE_KEY)
        val title = arguments?.getString(TITLE_KEY)
        val desc = arguments?.getString(DESCRIPTION_KEY)
        binding.tvTitle.setText(title)
        binding.tvDesc.setText(desc)
        binding.btnAdd.setOnClickListener {
            notesViewModel.updateNoteItem(UUID.fromString(id), binding.tvTitle.text.toString(), binding.tvDesc.text.toString())
            notesViewModel.notesList.observe(viewLifecycleOwner) {
                if (it != null) {
                    (requireContext().applicationContext as App).mySharedPreferense?.saveNotes(it)
                }
            }
            saveNotes()
        }

    }

    private fun saveNotes() {
        findNavController().navigateUp()
    }

    companion object {
        const val ID_KEY = "id"
        const val TITLE_KEY = "title"
        const val DATE_KEY = "date"
        const val DESCRIPTION_KEY = "description"
        fun navigateToNote(
            id: String,
            title: String,
            description: String,
            date: String,
            navController: NavController,
            destination: Int
        ) {
            navController.navigate(
                resId = destination,
                args = bundleOf(
                    ID_KEY to id,
                    TITLE_KEY to title,
                    DESCRIPTION_KEY to description,
                    DATE_KEY to date
                )
            )
        }
    }
}