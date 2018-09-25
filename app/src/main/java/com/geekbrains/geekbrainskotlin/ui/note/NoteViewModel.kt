package com.geekbrains.geekbrainskotlin.ui.note

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Error
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository = Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever { t ->
            t?.let {
                viewStateLiveData.value = when (t) {
                    is Success<*> -> NoteViewState(note = t.data as? Note)
                    is Error -> NoteViewState(error = t.error)
                }
            }
        }
    }
}