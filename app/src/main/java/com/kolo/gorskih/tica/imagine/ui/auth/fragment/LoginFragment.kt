package com.kolo.gorskih.tica.imagine.ui.auth.fragment

import android.content.Intent
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.interaction.AuthInteractor
import com.kolo.gorskih.tica.imagine.ui.base.BaseFragment
import com.kolo.gorskih.tica.imagine.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment() {

    private val authInteractor: AuthInteractor by inject()

    override fun getLayout(): Int = R.layout.fragment_login

    override fun initUi() {
        buttonLogin.setOnClickListener {
            val email = etEmailLogin.text.toString()
            val password = etPasswordLogin.text.toString()

            if (!email.isNullOrBlank() && !password.isNullOrBlank()) userSignIn(email, password)
            else toast(getString(R.string.checkInput))
        }

        linkRegister.setOnClickListener {
            activity?.showFragment(R.id.fragmentContainer, RegisterFragment(), false)
        }
    }

    private fun userSignIn(email: String, password: String) {
        authInteractor.loginWithEmailAndPassword(email, password) { isSuccessful, error ->
            if (isSuccessful) {
                toast(getString(R.string.loginSuccess))
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            } else {
                toast(error?.localizedMessage ?: getString(R.string.loginError))
            }
        }
    }

}