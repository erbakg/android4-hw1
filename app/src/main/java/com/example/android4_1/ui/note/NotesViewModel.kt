package com.example.android4_1.ui.note

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.UUID

class NotesViewModel : ViewModel() {
    var notesList = MutableLiveData<MutableList<Note>>()

    init {
        notesList.value = mutableListOf()
    }

    fun addNoteItem(newNoteItem: Note) {
        val list = notesList.value!!
        list!!.add(newNoteItem)
        notesList.postValue(list)
    }

    fun updateNoteItem(id: UUID, title: String, description: String) {
        val list = notesList.value!!

       val note = list!!.find{
           it.id == id
       }!!
        note.title = title
        note.description = description
        note.date = LocalDate.now()
    }

}