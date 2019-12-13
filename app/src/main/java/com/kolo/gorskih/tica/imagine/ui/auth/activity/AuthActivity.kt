package com.kolo.gorskih.tica.imagine.ui.auth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.ui.auth.fragment.LoginFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        showFragment(R.id.fragmentContainer, LoginFragment(), false)
    }
}