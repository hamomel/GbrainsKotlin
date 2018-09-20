package com.geekbrains.geekbrainskotlin.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.NoteResult
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Error
import com.geekbrains.geekbrainskotlin.data.model.NoteResult.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel
import kotlin.reflect.KProperty

class MainViewModel(val repository: Repository = Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult>{
        override fun onChanged(t: NoteResult?) {
            if (t == null) return

            when(t) {
                is Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
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

class Delegate(private val repository: Repository) {
    operator fun getValue(ref: Any?, property: KProperty<*>): LiveData<NoteResult> {
        return repository.getNotes()
    }
}