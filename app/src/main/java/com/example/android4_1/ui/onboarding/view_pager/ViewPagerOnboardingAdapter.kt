package com.example.android4_1.ui.onboarding.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android4_1.ui.onboarding.view_pager.content.AppFeaturesFragment
import com.example.android4_1.ui.onboarding.view_pager.content.AppInfoFragment
import com.example.android4_1.ui.onboarding.view_pager.content.GreetingFragment

class ViewPagerOnboardingAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: List<Fragment> = listOf(
        GreetingFragment(),
        AppInfoFragment(),
        AppFeaturesFragment(),
    )
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}