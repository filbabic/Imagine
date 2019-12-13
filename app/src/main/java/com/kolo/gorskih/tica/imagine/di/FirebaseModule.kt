package com.kolo.gorskih.tica.imagine.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

/**
 * Prepares Firebase related dependencies.
 */
fun firebaseModule() = module {
    single { FirebaseStorage.getInstance() }
    single { get<FirebaseStorage>().reference } // base storage

    single { FirebaseAuth.getInstance() }
}