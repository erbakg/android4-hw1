package com.example.android4_1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity
data class Project(
    @PrimaryKey()
    @ColumnInfo("project_id")
    val projectId: UUID = UUID.randomUUID(),
    @ColumnInfo("project_name")
    val projectName: String,
    val projectDate: String,
    val projectImg: String,
    @ColumnInfo("project_type")
    val projectType: ProjectTypes?
)


enum class ProjectTypes() {
    HOME,
    WORK,
    SCHOOL
}