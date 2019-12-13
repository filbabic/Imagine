package com.kolo.gorskih.tica.imagine.ui.auth.fragment

import android.content.Intent
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.interaction.AuthInteractor
import com.kolo.gorskih.tica.imagine.ui.base.BaseFragment
import com.kolo.gorskih.tica.imagine.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject

class RegisterFragment : BaseFragment() {

    private val authInteractor: AuthInteractor by inject()

    override fun getLayout(): Int = R.layout.fragment_register

    override fun initUi() {
        buttonRegister.setOnClickListener {
            val email = etEmailRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val passwordAgain = etPasswordRegisterRepeat.text.toString()

            if (!email.isNullOrBlank() && !password.isNullOrBlank() && !passwordAgain.isNullOrBlank()) {
                if (password == passwordAgain) registerUser(email, password)
                else toast(getString(R.string.passwords_dont_match))
            } else toast(getString(R.string.checkInput))
        }

        linkLogin.setOnClickListener {
            activity?.showFragment(R.id.fragmentContainer, LoginFragment(), false)
        }
    }

    private fun registerUser(email: String, password: String) {
        authInteractor.createUserWithEmailAndPassword(email, password) { isSuccessful, error ->

            if (isSuccessful) {
                toast(getString(R.string.RegistrationSuccess))
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            } else {
                toast(error?.localizedMessage ?: getString(R.string.registerError))
            }
        }
    }
}