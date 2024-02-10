package com.example.android4_1.ui.edit_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android4_1.App
import com.example.android4_1.databinding.FragmentEditPofileBinding
import com.example.android4_1.ui.note.NoteFragment

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditPofileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPofileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString(EditProfileFragment.NAME_KEY)
        val login = arguments?.getString(EditProfileFragment.LOGIN_KEY)
        val avatar =
            (requireContext().applicationContext as App).mySharedPreferense?.getSavedAvatar()
        if (name == "Enter name") {
            binding.etProfileName.setText("")
        } else {
            binding.etProfileName.setText(name)
        }
        if (login == "Enter login") {
            binding.etProfileLogin.setText("")
        } else {
            binding.etProfileLogin.setText(login)
        }
        Glide.with(this).load(avatar).circleCrop().into(binding.ivProfile);
        initListeners()
    }

    private fun initListeners() {
        binding.btnEditProfileAvatar.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        binding.btnSaveProfile.setOnClickListener {
            val myPrefs = (requireContext().applicationContext as App).mySharedPreferense
            myPrefs?.saveName(binding.etProfileName.text.toString())
            myPrefs?.saveLogin(binding.etProfileLogin.text.toString())
            findNavController().navigateUp()
        }
    }

    private val changeImage =
        this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                (requireContext().applicationContext as App).mySharedPreferense?.saveAvatar(imgUri.toString())
                Glide.with(this).load(imgUri).circleCrop().into(binding.ivProfile);
            }
        }

    companion object {
        const val NAME_KEY = "name"
        const val LOGIN_KEY = "login"
        const val AVATAR_KEY = "avatar"
        fun navigateToNote(
            name: String,
            login: String,
            navController: NavController,
            destination: Int
        ) {
            navController.navigate(
                resId = destination,
                args = bundleOf(
                    NAME_KEY to name,
                    LOGIN_KEY to login,
                )
            )
        }
    }

}