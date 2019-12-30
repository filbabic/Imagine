package com.kolo.gorskih.tica.imagine.common

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.showFragment(containerId: Int, fragment: Fragment, addToBackstack : Boolean){
    this.supportFragmentManager.beginTransaction().apply {
        if(addToBackstack) addToBackStack(fragment.tag)
    }.replace(containerId,fragment).commitAllowingStateLoss()
}

fun Fragment.toast(message: String) = Toast.makeText(this.context,message, Toast.LENGTH_LONG).show()
fun Activity.toast(message: String) = Toast.makeText(this,message, Toast.LENGTH_LONG).show()
