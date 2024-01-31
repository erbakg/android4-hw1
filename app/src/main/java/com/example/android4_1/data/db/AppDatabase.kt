package com.example.android4_1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android4_1.data.dao.NoteDao
import com.example.android4_1.data.dao.ProjectDao
import com.example.android4_1.data.entities.Note
import com.example.android4_1.data.entities.Project

@Database(
    entities = [Note::class,
        Project::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun projectDao(): ProjectDao
}