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
import java.io.*


/**
 * Used to handle Image uploading to Firebase.
 */
class UploadActivity : AppCompatActivity() {

    private val storageInteractor: StorageInteractor by inject()

    companion object {
        private const val KEY_FILE_PATH = "file_path"
        private const val KEY_IS_FROM_GALLERY = "is_from_gallery"

        fun newIntent(context: Context, filePath: String, isFromGallery: Boolean) =
            Intent(context, UploadActivity::class.java).apply {
                putExtra(KEY_FILE_PATH, filePath)
                putExtra(KEY_IS_FROM_GALLERY, isFromGallery)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filePath = intent?.getStringExtra(KEY_FILE_PATH) ?: ""
        if (filePath.isBlank()) finish()
        val isFromGallery = intent?.getBooleanExtra(KEY_IS_FROM_GALLERY, false) ?: false

        val file =
            if (isFromGallery)
                File(getImagePathFromInputStreamUri(Uri.parse(filePath)))
            else
                File(filePath)

        GlobalScope.launch(Dispatchers.Main) {
            val isSuccessfulUpload = storageInteractor.uploadImage(Uri.fromFile(file))

            if (isSuccessfulUpload) {
                Toast.makeText(this@UploadActivity, "Upload finished", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UploadActivity, "Upload failed", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    fun getImagePathFromInputStreamUri(uri: Uri): String {
        var inputStream: InputStream? = null
        var filePath: String? = null
        val lastSegment = uri.lastPathSegment

        if (uri.authority != null) {
            try {
                inputStream = contentResolver.openInputStream(uri) // context needed
                val photoFile = createTemporalFileFrom(inputStream, lastSegment)

                filePath = photoFile!!.path

            } catch (e: FileNotFoundException) {
                // log
            } catch (e: IOException) {
                // log
            } finally {
                try {
                    inputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return filePath ?: "unknown.jpg"
    }

    private fun createTemporalFileFrom(
        inputStream: InputStream?,
        lastSegment: String?
    ): File? {
        var targetFile: File? = null

        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)

            targetFile = createTemporalFile(lastSegment)
            val outputStream = FileOutputStream(targetFile)

            read = inputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                read = inputStream.read(buffer)
            }
            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return targetFile
    }

    private fun createTemporalFile(lastSegment: String?): File {
        return File(externalCacheDir, lastSegment) // context needed
    }
}