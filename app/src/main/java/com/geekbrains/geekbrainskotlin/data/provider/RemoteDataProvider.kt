package com.geekbrains.geekbrainskotlin.data.provider

import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.geekbrains.geekbrainskotlin.data.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {

    fun subscribeToAllNotes(): ReceiveChannel<Result>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note) : Note

    suspend fun getCurrentUser(): User?

    suspend fun deleteNote(noteId: String)
}