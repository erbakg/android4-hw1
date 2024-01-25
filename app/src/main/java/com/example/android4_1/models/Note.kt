package com.example.android4_1.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity
data class Note(
    var title: String,
    var description: String,
    var date: String,
    var inProgress:Boolean,
    var done: Boolean,
    @PrimaryKey()
    var id: UUID = UUID.randomUUID(),

)
{

}




