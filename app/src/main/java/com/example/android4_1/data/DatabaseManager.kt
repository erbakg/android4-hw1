package com.example.android4_1.data

import android.content.Context
import androidx.room.Room
import com.example.android4_1.data.dao.NoteDao
import com.example.android4_1.data.dao.ProjectDao
import com.example.android4_1.data.db.AppDatabase

object DatabaseManager {

    lateinit var noteDao: NoteDao
    lateinit var projectDao: ProjectDao

    fun init(context: Context){
        val database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notes"
        )
            .allowMainThreadQueries()
            .build()

        noteDao = database.noteDao()
        projectDao = database.projectDao()
    }
}