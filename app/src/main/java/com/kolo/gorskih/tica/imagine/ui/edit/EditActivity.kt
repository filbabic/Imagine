package com.kolo.gorskih.tica.imagine.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.EDITING_IMAGE_URL
import com.kolo.gorskih.tica.imagine.common.toast
import kotlinx.android.synthetic.main.activity_edit.*
import org.koin.android.ext.android.inject

class EditActivity : AppCompatActivity() {

    private val firebaseStorageReference by inject<StorageReference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        initView()
        initListeners()
    }

    private fun initView(){
        val path = firebaseStorageReference.child(intent.getStringExtra(EDITING_IMAGE_URL)!!)
        Glide.with(ivEditImage).load(path).fitCenter().into(ivEditImage)
    }

    private fun initListeners(){
        btnUpload.setOnClickListener {
            //TODO: uploadaj sliku
            toast(getString(R.string.uploading))
        }
        btnCancel.setOnClickListener { finish() }
    }
}
