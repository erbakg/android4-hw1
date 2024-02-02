package com.example.android4_1.ui.bottom_sheet

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.android4_1.App
import com.example.android4_1.R
import com.example.android4_1.data.DatabaseManager
import com.example.android4_1.data.entities.Project
import com.example.android4_1.data.entities.ProjectTypes
import com.example.android4_1.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var selectedDate: LocalDateTime? = null
    private var selectedAvatar: String? = null
    private var selectedPosition: Int? = null
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initViews()
    }

    private fun initViews() {
        val adapter =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    it.resources.getStringArray(R.array.project_types_array)
                )
            }
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun setListeners() {
        binding.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
                    val c1 = Calendar.getInstance()
                    c1.set(year, monthOfYear, dayOfMonth);
                    var strDate = LocalDateTime.ofInstant(c1.toInstant(), c1.timeZone.toZoneId())
                    selectedDate = strDate
                    binding.etDate.setText(
                        LocalDateTime.parse(strDate.toString()).format(formatter).toString()
                    )
                }, year, month, day
            )
            datePickerDialog.show()
        }
        binding.btnSaveProject.setOnClickListener {
            val resourceId = R.drawable.app_features_onboarding
            val project = Project(
                projectName = binding.etProjectName.text.toString(),
                projectDate = selectedDate.toString(),
                projectImg = selectedAvatar
                    ?: "android.resource://${context?.packageName}/$resourceId",
                projectType = when(selectedPosition) {
                    0 -> ProjectTypes.HOME
                    1 -> ProjectTypes.WORK
                    2 -> ProjectTypes.SCHOOL
                    else -> {
                        Toast.makeText(context, "Please select project type", Toast.LENGTH_SHORT)
                        null
                    }
                }
            )
            DatabaseManager.projectDao.add(project)
            dialog?.hide()
        }

        binding.btnEditProjectAvatar.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
        binding.etProjectName.setOnEditorActionListener { view, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    val inputMethodManager =
                        requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                    // on below line hiding our keyboard.
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }

                else -> {
                    false
                }
            }

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
                selectedAvatar = imgUri.toString()
                Glide.with(this).load(imgUri).circleCrop().into(binding.ivProjectAvatar);
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // used to show the bottom sheet dialog
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}