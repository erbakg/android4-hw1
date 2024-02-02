package com.example.android4_1.ui.home.pager_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android4_1.R
import com.example.android4_1.data.DatabaseManager
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.ProjectTypes
import com.example.android4_1.databinding.FragmentHomeBinding
import com.example.android4_1.databinding.FragmentHomeProjectsBinding
import com.example.android4_1.databinding.FragmentSchoolProjectsBinding
import com.example.android4_1.databinding.FragmentWorkProjectsBinding
import com.example.android4_1.ui.home.ProjectAdapter

class SchoolProjectsFragment : Fragment() {

    private var _binding: FragmentSchoolProjectsBinding? = null
    private val binding get() = _binding!!
    private val projectAdapter = ProjectAdapter(itemUpdated = this::itemUpdated)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSchoolProjectsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initListeners() {
        DatabaseManager.projectDao.getProjectsByType(ProjectTypes.SCHOOL).observe(viewLifecycleOwner) { data ->
            projectAdapter.submitList(data)
        }
    }

    private fun initViews() {
        binding.rvProjectGroup.adapter = projectAdapter
    }


    private fun itemUpdated(note: Note) {
        DatabaseManager.noteDao.updateNoteItem(note)
    }
}