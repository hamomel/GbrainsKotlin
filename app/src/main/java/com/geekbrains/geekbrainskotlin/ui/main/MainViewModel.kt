package com.geekbrains.geekbrainskotlin.ui.main

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result.Error
import com.geekbrains.geekbrainskotlin.data.model.Result.Success
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is Success<*> -> setData(it.data as? List<Note>)
                    is Error -> setError(it.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}
