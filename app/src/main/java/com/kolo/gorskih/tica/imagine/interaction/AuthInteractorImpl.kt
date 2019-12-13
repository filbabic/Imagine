package com.kolo.gorskih.tica.imagine.interaction

import com.google.firebase.auth.FirebaseAuth

/**
 * Communicates to the [FirebaseAuth] service.
 */

private const val UNKNOWN_USER_ID = "unknown"

class AuthInteractorImpl(private val firebaseAuth: FirebaseAuth) : AuthInteractor {

    override fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onFinished: (Boolean, Throwable?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onFinished(true, null)
            }.addOnFailureListener {
                onFinished(false, it)
            }
    }

    override fun loginWithEmailAndPassword(
        email: String,
        password: String,
        onFinished: (Boolean, Throwable?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onFinished(true, null)
            }
            .addOnFailureListener {
                onFinished(false, it)
            }
    }

    override fun logOut() = firebaseAuth.signOut()

    override fun isLoggedIn(): Boolean = getUserId().isNotBlank() && getUserId() != UNKNOWN_USER_ID

    override fun getUserId(): String = firebaseAuth.currentUser?.uid ?: UNKNOWN_USER_ID
}