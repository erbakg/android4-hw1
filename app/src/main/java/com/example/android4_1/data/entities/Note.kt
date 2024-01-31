package com.example.android4_1.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["project_id"],
            childColumns = ["project_id"],
        )
    ]
)
data class Note(
    @ColumnInfo("title")
    var title: String,
    @ColumnInfo("description")
    var description: String,
    @ColumnInfo("date")
    var date: String,
    @ColumnInfo("note_types")
    val type: NoteTypes = NoteTypes.TO_DO,
    @PrimaryKey()
    @ColumnInfo("note_id")
    var id: UUID = UUID.randomUUID(),
    @ColumnInfo("project_id")
    var projectId: UUID,
)

enum class NoteTypes(val typeId: Int) {
    TO_DO(0),
    IN_PROGRESS(1),
    DONE(2)
}





