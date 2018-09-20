package com.geekbrains.geekbrainskotlin.data

import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.provider.FireStoreProvider
import com.geekbrains.geekbrainskotlin.data.provider.RemoteDataProvider

object Repository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

}

