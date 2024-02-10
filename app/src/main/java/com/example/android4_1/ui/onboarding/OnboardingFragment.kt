package com.example.android4_1.ui.onboarding

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android4_1.App
import com.example.android4_1.databinding.FragmentOnboardingBinding
import com.example.android4_1.ui.onboarding.view_pager.ViewPagerOnboardingAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


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
            askNotificationPermissions()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val requestPermissionLauncher =
        this.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
               printToken()
            } else {
                requireActivity().finish()
            }
        }

    private fun printToken() {
        val auth = Firebase.auth
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            if(!it.isSuccessful){
                Log.w("haha", "FCM token is ${it.exception}")
                return@OnCompleteListener
            } else {
                val token = it.result
                Log.e("haha", "FCM token is ${token}")
                if(auth.currentUser == null){
                    restartApp()
                } else {
                    (requireContext().applicationContext as App).mySharedPreferense?.setOnboardingShown()
                    restartApp()
                }
            }
        })
    }

    private fun restartApp(){
        val intent = requireActivity().intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun askNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                (requireContext().applicationContext as App).mySharedPreferense?.setOnboardingShown()
                restartApp()
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Please provide permission")
                    .setPositiveButton("Okey") {_,_ ->
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }.setNegativeButton("No"){_,_ ->
                        requireActivity().finish()
                    }.show()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}