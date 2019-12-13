package com.kolo.gorskih.tica.imagine.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kolo.gorskih.tica.imagine.interaction.AuthInteractor
import com.kolo.gorskih.tica.imagine.ui.auth.activity.AuthActivity
import com.kolo.gorskih.tica.imagine.ui.main.MainActivity
import org.koin.android.ext.android.inject

/**
 * Navigates to the rest of the app, depending on whether or not the user is logged in.
 */
class SplashActivity : AppCompatActivity() {

    private val authInteractor: AuthInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = authInteractor.getUserId()

        if (userId.isNotBlank()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}