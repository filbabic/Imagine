package com.kolo.gorskih.tica.imagine.ui.edit

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
        if(checkPermissionForReadExternalStorage()){
            val path = firebaseStorageReference.child(intent.getStringExtra(EDITING_IMAGE_URL)!!)

            //this is temp, just to see what can be done, it
            val gpuImage = GPUImage(this)
            val tempFile = File(this.externalMediaDirs.first(), System.currentTimeMillis().toString())
            path.getFile(tempFile).addOnSuccessListener {
                val saveBitmap = BitmapFactory.decodeFile(tempFile.absolutePath)
                gpuImage.setFilter(GPUImageSepiaToneFilter())
                gpuImage.setImage(saveBitmap)
                val imageFileName = System.currentTimeMillis().toString()
                ImageStorageManager.saveToInternalStorage(this,gpuImage.bitmapWithFilterApplied,imageFileName)
                ivEditImage.setImageBitmap(ImageStorageManager.getImageFromInternalStorage(this,imageFileName))
            }
        }else{
            requestPermissionForReadExternalStorage()
            initView()
        }
    }

    private fun initListeners(){
        btnUpload.setOnClickListener {
            //TODO: uploadaj sliku
            toast(getString(R.string.uploading))
        }
        btnCancel.setOnClickListener { finish() }
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
        val READ_STORAGE_PERMISSION_REQUEST_CODE=0x3
        try {
            ActivityCompat.requestPermissions(
                this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}


