package com.kolo.gorskih.tica.imagine.ui.edit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.common.EDITING_IMAGE_URL
import com.kolo.gorskih.tica.imagine.common.ImageStorageManager
import com.kolo.gorskih.tica.imagine.common.toast
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.activity_edit.*
import org.koin.android.ext.android.inject
import java.io.File

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

        val gpuImage = GPUImage(this)
        val tempFile = File(this.externalMediaDirs.first(), System.currentTimeMillis().toString())
        path.getFile(tempFile).addOnSuccessListener {
            val saveBitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
            gpuImage.setFilter(GPUImageSepiaToneFilter())
            gpuImage.setRotation(Rotation.ROTATION_270)
            gpuImage.setImage(saveBitmap)
            val imageFileName = System.currentTimeMillis().toString()
            ImageStorageManager.saveToInternalStorage(this,gpuImage.bitmapWithFilterApplied,imageFileName)
            ivEditImage.setImageBitmap(ImageStorageManager.getImageFromInternalStorage(this,imageFileName))
        }
    }

    private fun initListeners(){
        btnUpload.setOnClickListener {
            //TODO: uploadaj sliku
            toast(getString(R.string.uploading))
        }
        btnCancel.setOnClickListener { finish() }
    }
}
