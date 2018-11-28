package com.geekbrains.geekbrainskotlin.ui.splash

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}