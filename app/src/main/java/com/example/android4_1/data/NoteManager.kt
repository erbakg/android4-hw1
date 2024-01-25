package com.example.android4_1.data

import android.content.Context
import androidx.room.Room

object NoteManager {

   private var _dao: NoteDao? = null
    val  dao get() = _dao!!

    fun init(context: Context){
        val database = Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "notes"
        )
            .allowMainThreadQueries()
            .build()

        _dao = database.noteDao()
    }
}