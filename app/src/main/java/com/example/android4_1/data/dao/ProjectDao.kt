package com.example.android4_1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.android4_1.data.entities.Project
import com.example.android4_1.data.entities.ProjectAndNotes

@Dao
interface ProjectDao {
    @Insert
    fun add(project: Project)

    @Delete
    fun remove(project: Project)

    @Update
    fun update(project: Project)

    @Query("SELECT * FROM project")
    fun getProjects(): List<Project>

    @Transaction
    @Query("SELECT * FROM project")
    fun getProjectsWithNotes():LiveData<List<ProjectAndNotes>>
}