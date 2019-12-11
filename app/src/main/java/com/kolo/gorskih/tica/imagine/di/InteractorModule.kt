package com.kolo.gorskih.tica.imagine.di

import com.kolo.gorskih.tica.imagine.storage.StorageInteractor
import com.kolo.gorskih.tica.imagine.storage.StorageInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Loads all the interaction dependencies for Firebase.
 */


fun interactionModule() = module {
    single { StorageInteractorImpl(get(), androidContext()) as StorageInteractor }
}