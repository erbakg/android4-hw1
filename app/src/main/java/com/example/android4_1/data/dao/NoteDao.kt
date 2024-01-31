package com.example.android4_1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.NoteTypes
import java.util.UUID

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
    @Query("SELECT * FROM note WHERE note_types=:notesType")
    fun getNotesByType(notesType: NoteTypes): LiveData<List<Note>>
    @Delete
    fun removeNote(note: Note)

    @Query("SELECT * FROM note WHERE project_id=:projectId")
    fun getNotesByProject(projectId: UUID): List<Note>
}