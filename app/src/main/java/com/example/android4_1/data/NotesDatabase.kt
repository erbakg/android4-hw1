package com.example.android4_1.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android4_1.models.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}