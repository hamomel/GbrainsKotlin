package com.geekbrains.geekbrainskotlin

import android.support.multidex.MultiDexApplication
import com.geekbrains.geekbrainskotlin.di.appModule
import com.geekbrains.geekbrainskotlin.di.mainModule
import com.geekbrains.geekbrainskotlin.di.noteModule
import com.geekbrains.geekbrainskotlin.di.splashModule
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}