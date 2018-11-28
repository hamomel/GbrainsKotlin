package com.geekbrains.geekbrainskotlin.ui.note

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel
import com.geekbrains.geekbrainskotlin.ui.note.NoteViewState.NoteData
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                repository.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }
}