package com.example.android4_1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android4_1.App
import com.example.android4_1.R
import com.example.android4_1.data.DatabaseManager
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.Project
import com.example.android4_1.databinding.FragmentHomeBinding
import com.example.android4_1.ui.bottom_sheet.BottomSheetFragment
import java.time.LocalDateTime
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val projectAdapter = ProjectAdapter(itemUpdated = this::itemUpdated)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initData()
        initViews()
        initOnboarding()
    }

    private fun initOnboarding() {
        val onboardingIsShown =
            (requireContext().applicationContext as App).mySharedPreferense?.getOnboardingShownStatus()
        if (onboardingIsShown == true) {
            return
        } else {
            findNavController().navigate(R.id.action_navigation_home_to_onboardingFragment)
        }
    }

    private fun initViews() {
        binding.rvProjectGroup.adapter = projectAdapter
    }

    private fun initData() {
        val projectDao = DatabaseManager.projectDao
        val projects = projectDao.getProjects()
        if (projects.size == 0) {
            val resourceId = R.drawable.app_features_onboarding
            projectDao.add(
                Project(
                    projectName = "To Do",
                    projectDate = LocalDateTime.now().toString(),
                    projectImg = "android.resource://${context?.packageName}/$resourceId"
                )
            )
        }
    }

    private fun initListeners() {
        binding.btnAddNote.setOnClickListener {
            val modal = BottomSheetFragment()
            fragmentManager.let {
                if (it != null) {
                    modal.show(it, BottomSheetFragment.TAG)
                }
            }
        }
        DatabaseManager.projectDao.getProjectsWithNotes().observe(viewLifecycleOwner) { data ->
            projectAdapter.submitList(data)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun itemUpdated(note: Note) {
        DatabaseManager.noteDao.updateNoteItem(note)
    }
}