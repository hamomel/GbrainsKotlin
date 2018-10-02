package com.geekbrains.geekbrainskotlin

import android.app.Application
import com.geekbrains.geekbrainskotlin.di.appModule
import com.geekbrains.geekbrainskotlin.di.mainModule
import com.geekbrains.geekbrainskotlin.di.noteModule
import com.geekbrains.geekbrainskotlin.di.splasModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splasModule, mainModule, noteModule))
    }
}