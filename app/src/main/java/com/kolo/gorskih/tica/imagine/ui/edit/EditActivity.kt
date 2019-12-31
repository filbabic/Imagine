package com.kolo.gorskih.tica.imagine.ui.edit

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.EDITING_IMAGE_URL
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractor
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

const val READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3

class EditActivity : AppCompatActivity() {

    private val storageInteractor by inject<StorageInteractor>()
    private var currentImage: Bitmap? = null
    private var editImagePath: String = ""
    private var photoEditor: PhotoEditor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        editImagePath = intent.getStringExtra(EDITING_IMAGE_URL) ?: ""
        initListeners()

        initView()
    }

    private fun initView() {
        if (checkPermissionForReadExternalStorage()) {
            GlobalScope.launch {
                val bitmap = storageInteractor.getDownloadedImageFromPath(editImagePath)
                currentImage = bitmap

                launch(Dispatchers.Main) {
                    photoEditorView.source.setImageBitmap(bitmap)
                    buildPhotoEditor()
                }
            }
        } else {
            requestPermissionForReadExternalStorage()
            initView()
        }
    }

    private fun buildPhotoEditor() {
        photoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .build()

        photoEditor?.run {
            setBrushDrawingMode(true)
        }
    }

    private fun initListeners() {
        btnUpload.setOnClickListener { saveAndUploadImage() }
        btnCancel.setOnClickListener { finish() }

        // TODO rename to draw
        btnCropResize.setOnClickListener { showCropResizeDialog() }
        btnFilters.setOnClickListener { showFiltersDialog() }

        // TODO rename to emoji
        btnEditPixel.setOnClickListener { showEmojiDialog() }
    }

    private fun showFiltersDialog() {
        val dialog = FiltersListDialog().apply {
            onFilterSelected = ::onFilterSelected
        }

        dialog.show(supportFragmentManager, "filters")
    }

    // TODO replace with drawing + text on screen.
    private fun showCropResizeDialog() {

    }

    // TODO show a grid of emoji and add them to the image, resize/rotate
    private fun showEmojiDialog() {

    }

    private fun saveAndUploadImage() {
        photoEditor?.saveAsBitmap(object : OnSaveBitmap {
            override fun onFailure(e: java.lang.Exception?) {
                toast("Upload failed")
            }

            override fun onBitmapReady(saveBitmap: Bitmap?) {
                val image = saveBitmap ?: return

                GlobalScope.launch {
                    val localFile = storageInteractor.saveLocalImageToFile(image)
                    launch(Dispatchers.Main) { toast(getString(R.string.uploading)) }

                    val isSuccessful = storageInteractor.uploadImage(Uri.fromFile(localFile))

                    launch(Dispatchers.Main) {
                        if (isSuccessful) {
                            toast("Upload finished")

                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            toast("Upload failed")
                        }
                    }
                }
            }
        })
    }

    private fun onFilterSelected(photoFilter: PhotoFilter) {
        photoEditor?.setFilterEffect(photoFilter)
    }

    private fun checkPermissionForReadExternalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result: Int =
                this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    private fun requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}


