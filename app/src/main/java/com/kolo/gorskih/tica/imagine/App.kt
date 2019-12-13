package com.kolo.gorskih.tica.imagine

import android.app.Application
import com.kolo.gorskih.tica.imagine.di.firebaseModule
import com.kolo.gorskih.tica.imagine.di.interactionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Creates everything needed for DI and the project to work.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // setup

        startKoin {
            androidContext(this@App)

            modules(listOf(firebaseModule(), interactionModule()))
        }
    }
}