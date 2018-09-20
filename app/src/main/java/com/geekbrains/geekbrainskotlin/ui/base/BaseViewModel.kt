package com.geekbrains.geekbrainskotlin.ui.base

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

open class BaseViewModel<T, S : BaseViewState<T>> : ViewModel() {

    open val viewStateLiveData = MutableLiveData<S>()

    open fun getViewSatate(): LiveData<S> = viewStateLiveData
}