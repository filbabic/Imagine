package com.kolo.gorskih.tica.imagine.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.ui.camera.CameraActivity
import com.kolo.gorskih.tica.imagine.ui.upload.UploadActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_CODE_IMAGE_CAPTURE = 51
const val KEY_PATH = "path"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO for now, we just capture something and display it here, change this once
        // TODO there's more implementation
        startActivityForResult(
            Intent(this, CameraActivity::class.java),
            REQUEST_CODE_IMAGE_CAPTURE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(KEY_PATH) ?: ""

            startActivity(UploadActivity.newIntent(this, filePath))
            return
            if (filePath.isNotBlank()) {
                Glide.with(this)
                    .load(filePath)
                    .into(capturedImage)
            }
        }
    }
}
