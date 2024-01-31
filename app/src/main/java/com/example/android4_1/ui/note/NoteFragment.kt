package com.example.android4_1.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.android4_1.data.DatabaseManager
import com.example.android4_1.databinding.FragmentNoteBinding
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.NoteTypes
import java.time.LocalDate
import java.util.UUID

class NoteFragment : Fragment() {
    private var selectedPosition: Int? = null
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViews()
    }

    private fun initListeners() {
        val projects = DatabaseManager.projectDao.getProjects()
        val id = arguments?.getString(ID_KEY)
        val projectId = arguments?.getString(PROJECT_ID_KEY)
        val title = arguments?.getString(TITLE_KEY)
        val desc = arguments?.getString(DESCRIPTION_KEY)
        binding.tvTitle.setText(title)
        binding.tvDesc.setText(desc)

        binding.btnAdd.setOnClickListener {
            DatabaseManager.noteDao.addNoteItem(
                Note(
                    title = binding.tvTitle.text.toString(),
                    description = binding.tvDesc.text.toString(),
                    id = UUID.fromString(id),
                    date = LocalDate.now().toString(),
                    type = NoteTypes.TO_DO,
                    projectId = if (projectId == "") {
                        projects[selectedPosition!!].projectId
                    } else {
                        Toast.makeText(context, "Please select project id", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                )
            )
            navigateUp()
        }
    }

    private fun initViews() {
        val projects = DatabaseManager.projectDao.getProjects()
        val spinnerList = ArrayList<String>()
        for (project in projects) {
            spinnerList.add(project.projectName)
        }
        val adapter = context?.let {
            ArrayAdapter(
                it, android.R.layout.simple_spinner_item, spinnerList
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
                binding.tvProject.setText(projects[position].projectName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

    companion object {
        const val ID_KEY = "id"
        const val TITLE_KEY = "title"
        const val PROJECT_ID_KEY = "project_id"
        const val DESCRIPTION_KEY = "description"
        fun navigateToNote(
            id: String,
            title: String,
            description: String,
            projectId: String,
            navController: NavController,
            destination: Int
        ) {
            navController.navigate(
                resId = destination,
                args = bundleOf(
                    ID_KEY to id,
                    TITLE_KEY to title,
                    DESCRIPTION_KEY to description,
                    PROJECT_ID_KEY to projectId
                )
            )
        }
    }
}