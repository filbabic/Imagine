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
import com.kolo.gorskih.tica.imagine.common.toast
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
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
        if(checkPermissionForReadExternalStorage()){
            val path = firebaseStorageReference.child(intent.getStringExtra(EDITING_IMAGE_URL)!!)
            path.downloadUrl.addOnSuccessListener {
                gpuImageView.setImage(it)
                gpuImageView.setFilter(GPUImageSepiaToneFilter())
                try {
                    gpuImageView.saveToPictures("GPUImageine","firstEver.png",null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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


