package com.geekbrains.geekbrainskotlin.data

import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.provider.RemoteDataProvider

class Repository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)

    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
    suspend fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
}

