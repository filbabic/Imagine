package com.kolo.gorskih.tica.imagine.ui.auth.fragment

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.ui.base.BaseFragment
import com.kolo.gorskih.tica.imagine.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(){

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun getLayout(): Int = R.layout.fragment_login

    override fun initUi() {
        buttonLogin.setOnClickListener {
            val email = etEmailLogin.text.toString()
            val password = etPasswordLogin.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank()) userSignIn(email,password)
            else toast(getString(R.string.checkInput))
        }

        linkRegister.setOnClickListener {
            activity?.showFragment(R.id.fragmentContainer,RegisterFragment(),false)
        }
    }

    private fun userSignIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                toast(getString(R.string.loginSuccess))
                val intent = Intent(activity!!, MainActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
            .addOnFailureListener{
                toast(it.localizedMessage ?: getString(R.string.loginError))
            }
    }

}