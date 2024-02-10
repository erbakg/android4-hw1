package com.example.android4_1

import android.R.attr.button
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.android4_1.databinding.ActivityMainBinding
import com.example.android4_1.ui.auth.AuthFragment
import com.example.android4_1.ui.onboarding.OnboardingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDrawerNav()
        initOnboarding()
    }

    private fun initDrawerNav() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.drawer_menu_open,
            R.string.drawer_menu_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initOnboarding() {
        val isUserAuthed =
            (applicationContext as App).mySharedPreferense?.getUserAuthed()
        val onboardingIsShown =
            (applicationContext as App).mySharedPreferense?.getOnboardingShownStatus()

        if (isUserAuthed == false) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, AuthFragment())
                .commit();
            hideNavAndActionBar()
        } else {
            if (onboardingIsShown == true) {
                val navView: BottomNavigationView = binding.navView
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home, R.id.navigation_notes_list, R.id.navigation_profile
                    ), binding.drawerLayout
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
                initListener()
            } else {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OnboardingFragment())
                    .commit();
                hideNavAndActionBar()
            }

        }

    }

    private fun hideNavAndActionBar() {
        supportActionBar?.hide()
        binding.navView.visibility = View.GONE
    }

    private fun initListener() {
        val pref = (applicationContext as App).mySharedPreferense
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
        val navigationView: NavigationView = findViewById(R.id.drawer_nav_view) as NavigationView
        val headerView: View = navigationView.getHeaderView(0)
        val logoutButton: Button = headerView.findViewById(R.id.btn_log_out) as Button
        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            pref?.setUserAuth(false)
            val intent = this.intent
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val name: String? = pref?.getSavedName()
        val login: String? = pref?.getSavedLogin()
        val avatar: String? = pref?.getSavedAvatar()
        val tvLogin: TextView = headerView.findViewById(R.id.tv_user_email) as TextView
        tvLogin.text = login
        val tvName: TextView = headerView.findViewById(R.id.tv_user_name) as TextView
        tvName.text = name ?: "Enter your name"
        val ivAvatar: ImageView = headerView.findViewById(R.id.iv_user_avatar) as ImageView
        if (avatar != null) {
            Glide.with(this).load(avatar).circleCrop().into(ivAvatar);
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp()
    }
}