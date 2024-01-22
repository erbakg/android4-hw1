package com.example.android4_1.ui.note

import android.R.attr.data
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.time.LocalDate
import java.util.UUID


class NotesViewModel : ViewModel() {
    var notesList = MutableLiveData<MutableList<Note>?>()
    val PREF = "PREF";
    val KEY = "KEY_DATA";

    init {
        notesList.value = mutableListOf()
    }

    fun addNoteItem(newNoteItem: Note) {
        val list = notesList.value!!
        list!!.add(newNoteItem)
        notesList.postValue(list)

    }

    fun updateNoteItem(id: UUID, title: String, description: String) {
        val list = notesList.value

        val note = list!!.find {
            it.id == id
        }!!
        note.title = title
        note.description = description
        note.date = LocalDate.now()
        note.inProgress = false
        note.done = false

        notesList.postValue(list)
    }

    fun changeNoteItemStatus(id: UUID) {
        val list = notesList.value

        val note = list!!.find {
            it.id == id
        }!!
        note.date = LocalDate.now()
        if (!note.done && !note.inProgress) {
            note.inProgress = true
        } else if (note.inProgress) {
            note.inProgress = false
            note.done = true
        }

        notesList.postValue(list)

    }
}