package com.example.android4_1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android4_1.models.Note

@Dao
interface NoteDao {
    @Insert
    fun addNoteItem(note: Note)

    @Update
    fun updateNoteItem(note: Note)

    @Update
    fun changeNoteItemStatus(note: Note)

    @Query("SELECT * FROM note")
    fun getNotes(): LiveData<List<Note>>
}