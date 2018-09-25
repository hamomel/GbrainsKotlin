package com.geekbrains.geekbrainskotlin.ui.main

import android.arch.lifecycle.Observer
import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.NoteResult
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Error
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel

class MainViewModel(val repository: Repository = Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> { t ->
        t?.let {
            viewStateLiveData.value = when (it) {
                is Success<*> -> MainViewState(notes = it.data as? List<Note>)
                is Error -> MainViewState(error = it.error)
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}
