package com.example.android4_1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android4_1.App
import com.example.android4_1.databinding.FragmentProfileBinding
import com.example.android4_1.R
import com.example.android4_1.ui.edit_profile.EditProfileFragment

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListeners()
    }

    private fun initListeners() {
        binding.btnEditProfile.setOnClickListener {
            val name: String? =
                (requireContext().applicationContext as App).mySharedPreferense?.getSavedName()
            val login: String? =
                (requireContext().applicationContext as App).mySharedPreferense?.getSavedLogin()
            val avatar: String? =
                (requireContext().applicationContext as App).mySharedPreferense?.getSavedAvatar()
            EditProfileFragment.navigateToNote(
               name = name ?: "Enter name",
                login ?: "Enter login",
                findNavController(),
                R.id.action_navigation_profile_to_editPofileFragment
            )
        }
    }

    private fun initUi() {
        val name: String? =
            (requireContext().applicationContext as App).mySharedPreferense?.getSavedName()
        val login: String? =
            (requireContext().applicationContext as App).mySharedPreferense?.getSavedLogin()
        val avatar: String? =
            (requireContext().applicationContext as App).mySharedPreferense?.getSavedAvatar()
        if (name != null) {
            binding.tvProfileName.text = name
        } else {
            binding.tvProfileName.text = "Enter your name"
        }
        if (login != null) {
            binding.tvProfileLogin.text = login
        } else {
            binding.tvProfileLogin.text = "Enter your login"
        }

        if (avatar != null) {
            Glide.with(this).load(avatar).circleCrop().into(binding.ivProfile);
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}