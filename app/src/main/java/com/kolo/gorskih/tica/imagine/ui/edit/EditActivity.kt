package com.kolo.gorskih.tica.imagine.ui.edit

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.EDITING_IMAGE_URL
import com.kolo.gorskih.tica.imagine.common.toast
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractor
import com.kolo.gorskih.tica.imagine.ui.upload.UploadActivity
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

    private var isInDrawMode: Boolean = false

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

        btnDraw.setOnClickListener { onDrawSelected() }
        btnFilters.setOnClickListener { showFiltersDialog() }
        btnEmoji.setOnClickListener { showEmojiDialog() }

        btnUndo.setOnClickListener { photoEditor?.undo() }
        btnRedo.setOnClickListener { photoEditor?.redo() }
    }

    private fun showFiltersDialog() {
        val dialog = FiltersListDialog().apply {
            onFilterSelected = ::onFilterSelected
        }

        dialog.show(supportFragmentManager, "filters")
    }

    private fun onDrawSelected() {
        isInDrawMode = !isInDrawMode
        photoEditor?.setBrushDrawingMode(isInDrawMode)

        btnDraw.setImageResource(if (isInDrawMode) R.drawable.ic_cancel_white_24dp else R.drawable.ic_edit_white_24dp)
    }

    private fun showEmojiDialog() {
        val dialog = EmojiListDialog().apply {
            onEmojiSelected = ::onEmojiSelected
        }

        dialog.show(supportFragmentManager, "emoji")
    }

    private fun onEmojiSelected(emoji: String) {
        photoEditor?.addEmoji(emoji)
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

                    startUpload(localFile.absolutePath)
                }
            }
        })
    }

    private fun startUpload(filePath: String) {
        GlobalScope.launch(Dispatchers.Main) {
            startActivity(
                UploadActivity.newIntent(
                    this@EditActivity,
                    filePath,
                    false
                )
            )

            finish()
        }
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


