package com.kolo.gorskih.tica.imagine.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.ui.auth.fragment.LoginFragment
import com.kolo.gorskih.tica.imagine.ui.main.MainActivity

class AuthActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if(isUserLoggedIn()){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        else showFragment(R.id.fragmentContainer,LoginFragment(),false)
    }



    private fun isUserLoggedIn():Boolean = firebaseAuth.currentUser!=null
}