package com.kolo.gorskih.tica.imagine.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.EDITING_IMAGE_URL
import kotlinx.android.synthetic.main.activity_edit.*
import org.koin.android.ext.android.inject

class EditActivity : AppCompatActivity() {

    private val firebaseStorage by inject<StorageReference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val path = firebaseStorage.child(intent.getStringExtra(EDITING_IMAGE_URL)!!)
        Glide.with(ivEditImage).load(path).fitCenter().into(ivEditImage)
    }
}
