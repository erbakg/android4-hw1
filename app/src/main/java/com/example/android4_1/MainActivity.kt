package com.example.android4_1

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android4_1.databinding.ActivityMainBinding
import com.example.android4_1.ui.onboarding.OnboardingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initOnboarding()
        initListener()
    }
    private fun initOnboarding() {
        val onboardingIsShown =
            (applicationContext as App).mySharedPreferense?.getOnboardingShownStatus()
        if (onboardingIsShown == true) {
            val navView: BottomNavigationView = binding.navView

            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_notes_list, R.id.navigation_profile
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        } else {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, OnboardingFragment())
                .commit();
        }
    }

    private fun initListener() {
        this.findNavController(R.id.nav_host_fragment_activity_main)
            .addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.onboardingFragment) {
                    this.supportActionBar?.hide()
                    binding.navView.visibility = View.GONE
                } else if (destination.id == R.id.noteFragment) {
                    binding.navView.visibility = View.GONE
                } else {
                    binding.navView.visibility = View.VISIBLE
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp()
    }
}