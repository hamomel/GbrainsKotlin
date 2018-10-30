package com.geekbrains.geekbrainskotlin.ui.main

import android.arch.lifecycle.Observer
import android.support.annotation.VisibleForTesting
import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.geekbrains.geekbrainskotlin.data.model.Result.Error
import com.geekbrains.geekbrainskotlin.data.model.Result.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel

class MainViewModel(val repository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<Result> { t ->
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

    @VisibleForTesting
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}
