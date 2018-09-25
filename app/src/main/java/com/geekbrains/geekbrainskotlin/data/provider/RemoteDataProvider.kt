package com.geekbrains.geekbrainskotlin.data.provider

import android.arch.lifecycle.LiveData
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.NoteResult
import com.geekbrains.geekbrainskotlin.data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>

    fun getCurrentUser(): LiveData<User?>
}