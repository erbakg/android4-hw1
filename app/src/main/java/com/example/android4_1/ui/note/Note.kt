package com.example.android4_1.ui.note

import java.time.LocalDate
import java.util.UUID

class Note(
    var title: String,
    var description: String,
    var date: LocalDate,
    var id: UUID = UUID.randomUUID(),

)
{

}



