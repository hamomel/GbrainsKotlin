package com.geekbrains.geekbrainskotlin.ui.note

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result.Error
import com.geekbrains.geekbrainskotlin.data.model.Result.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel
import com.geekbrains.geekbrainskotlin.ui.note.NoteViewState.Data

class NoteViewModel(val repository: Repository) : BaseViewModel<Data, NoteViewState>() {

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever { t ->
            t?.let {
                viewStateLiveData.value = when (t) {
                    is Success<*> -> NoteViewState(Data(note = t.data as? Note))
                    is Error -> NoteViewState(error = t.error)
                }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let {
            repository.deleteNote(it.id).observeForever { t ->
                t?.let {
                    viewStateLiveData.value = when (it) {
                        is Success<*> -> NoteViewState(Data(isDeleted = true))
                        is Error -> NoteViewState(error = it.error)
                    }
                }
            }
        }
    }
}