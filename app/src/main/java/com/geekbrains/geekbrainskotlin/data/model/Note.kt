package com.geekbrains.geekbrainskotlin.data.model

import java.util.*

class Note(var title: String,
           var note: String,
           var color: Int,
           val id: String = UUID.randomUUID().toString())
