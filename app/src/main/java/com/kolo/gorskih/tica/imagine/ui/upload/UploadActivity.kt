package com.kolo.gorskih.tica.imagine.ui.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File

/**
 * Used to handle Image uploading to Firebase.
 */
class UploadActivity : AppCompatActivity() {

    private val storageInteractor: StorageInteractor by inject()

    companion object {
        private const val KEY_FILE_PATH = "file_path"

        fun newIntent(context: Context, filePath: String) =
            Intent(context, UploadActivity::class.java).apply {
                putExtra(KEY_FILE_PATH, filePath)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filePath = intent?.getStringExtra(KEY_FILE_PATH) ?: ""
        if (filePath.isBlank()) finish()

        val file = File(filePath)

        GlobalScope.launch(Dispatchers.Main) {
            val isSuccessfulUpload = storageInteractor.uploadImage(Uri.fromFile(file))

            if (isSuccessfulUpload) {
                Toast.makeText(this@UploadActivity, "Upload finished", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@UploadActivity, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}