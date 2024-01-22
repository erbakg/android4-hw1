package com.example.android4_1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android4_1.databinding.FragmentHomeBinding
import com.example.android4_1.ui.home.view_pager.ViewPagerHomeAdapter
import com.example.android4_1.ui.note.Note
import com.example.android4_1.ui.note.NotesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NotesViewModel

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
        val activity = requireActivity()
        notesViewModel =
            ViewModelProvider(activity).get(NotesViewModel::class.java)
        initListiners()
        setRecyclerView()
    }

    private fun setRecyclerView() {


    }

    private fun initListiners() {
        binding.homeViewPager.adapter = ViewPagerHomeAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
           tab.text = when (position) {
                0 -> "All tasks"
                1 -> "In progress"
                else -> "Done"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}