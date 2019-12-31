package com.kolo.gorskih.tica.imagine.interaction

/**
 * Communicates to the FirebaseAuth service.
 */
interface AuthInteractor {

    fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onFinished: (Boolean, Throwable?) -> Unit
    )

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onFinished: (Boolean, Throwable?) -> Unit
    )

    fun getUserId(): String

    fun getUserEmail():String

    fun isLoggedIn(): Boolean

    fun logOut()
}