package com.example.android4_1.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android4_1.R
import com.example.android4_1.databinding.FragmentNotesListBinding
import com.example.android4_1.ui.note.NoteFragment
import com.example.android4_1.ui.notes.view_pager.ViewPagerNotesAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.util.UUID

class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.notesViewPager.adapter = ViewPagerNotesAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.notesTabLayout, binding.notesViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "In progress"
                1 -> "Done"
                else -> "To Do"
            }
        }.attach()
        binding.btnAddNote.setOnClickListener() {
            NoteFragment.navigateToNote(
                id = UUID.randomUUID().toString(),
                title = "",
                description = "",
                projectId = "",
                navController = findNavController(),
                destination = R.id.action_navigation_notes_list_to_noteFragment
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}