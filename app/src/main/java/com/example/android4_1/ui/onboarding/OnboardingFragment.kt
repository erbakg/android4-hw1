package com.example.android4_1.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android4_1.App
import com.example.android4_1.R
import com.example.android4_1.databinding.FragmentHomeBinding
import com.example.android4_1.databinding.FragmentOnboardingBinding
import com.example.android4_1.ui.home.view_pager.ViewPagerHomeAdapter
import com.example.android4_1.ui.onboarding.view_pager.ViewPagerOnboardingAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListiners()
    }

    private fun initListiners() {
        binding.onboardingViewPager.adapter =
            ViewPagerOnboardingAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(
            binding.onboardingTabLayout,
            binding.onboardingViewPager
        ) { tab, position ->
        }.attach()

        binding.btnNext.setOnClickListener {
            val viewPager = binding.onboardingViewPager
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }

        binding.btnSkip.setOnClickListener {
            (requireContext().applicationContext as App).mySharedPreferense?.setOnboardingShown()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}