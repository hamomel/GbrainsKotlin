package com.geekbrains.geekbrainskotlin.di

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.provider.FireStoreProvider
import com.geekbrains.geekbrainskotlin.data.provider.RemoteDataProvider
import com.geekbrains.geekbrainskotlin.ui.main.MainViewModel
import com.geekbrains.geekbrainskotlin.ui.note.NoteViewModel
import com.geekbrains.geekbrainskotlin.ui.splash.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splasModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}