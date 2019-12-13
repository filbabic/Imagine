package com.kolo.gorskih.tica.imagine.di

import com.kolo.gorskih.tica.imagine.interaction.AuthInteractor
import com.kolo.gorskih.tica.imagine.interaction.AuthInteractorImpl
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractor
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Loads all the interaction dependencies for Firebase.
 */
fun interactionModule() = module {
    single { StorageInteractorImpl(get(), get(), androidContext()) as StorageInteractor }
    single { AuthInteractorImpl(get()) as AuthInteractor }
}