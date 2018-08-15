package com.geekbrains.geekbrainskotlin.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.geekbrains.geekbrainskotlin.data.model.Repository

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val viewState: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewState.value = MainViewState(Repository.getNotes())
    }

    fun viewState(): LiveData<MainViewState> = viewState
}