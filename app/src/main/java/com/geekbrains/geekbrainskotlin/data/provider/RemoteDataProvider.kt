package com.geekbrains.geekbrainskotlin.data.provider

import android.arch.lifecycle.LiveData
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.geekbrains.geekbrainskotlin.data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<Result>
    fun getNoteById(id: String): LiveData<Result>
    fun saveNote(note: Note) : LiveData<Result>

    fun getCurrentUser(): LiveData<User?>

    fun deleteNote(noteId: String): LiveData<Result>
}