package com.kolo.gorskih.tica.imagine.ui.auth.fragment

import com.google.firebase.auth.FirebaseAuth
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.showFragment
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment(){

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun getLayout(): Int = R.layout.fragment_register

    override fun initUi() {
        buttonRegister.setOnClickListener {
            val email = etEmailRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val passwordAgain = etPasswordRegisterRepeat.text.toString()

            if(!email.isNullOrBlank() && !password.isNullOrBlank() && !passwordAgain.isNullOrBlank()){
                if(password == passwordAgain )registerUser(email,password)
                else toast(getString(R.string.passwords_dont_match))
            }
            else toast(getString(R.string.checkInput))
        }

        linkLogin.setOnClickListener {
            activity?.showFragment(R.id.fragmentContainer,LoginFragment(),false)
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                toast(getString(R.string.RegistrationSuccess))
                activity?.showFragment(R.id.fragmentContainer,LoginFragment(),false)
            }
            .addOnFailureListener {
                toast(it.localizedMessage)
            }
    }
}