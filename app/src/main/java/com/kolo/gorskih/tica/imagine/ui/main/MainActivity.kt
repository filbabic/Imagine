package com.kolo.gorskih.tica.imagine.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.Image
import com.kolo.gorskih.tica.imagine.MainUI
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.interaction.AuthInteractor
import com.kolo.gorskih.tica.imagine.interaction.StorageInteractor
import com.kolo.gorskih.tica.imagine.ui.auth.activity.AuthActivity
import com.kolo.gorskih.tica.imagine.ui.camera.CameraActivity
import com.kolo.gorskih.tica.imagine.ui.upload.UploadActivity
import com.kolo.gorskih.tica.imagine.ui.view_holders.imageItemModelHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


private const val REQUEST_CODE_IMAGE_CAPTURE = 51
private const val REQUEST_CODE_GALLERY = 52
const val KEY_PATH = "path"

class MainActivity : AppCompatActivity() {

    private val controller: MainActivityController = MainActivityController(this::onItemClick)
    private val authInteractor: AuthInteractor by inject()
    private val storageInteractor: StorageInteractor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = GridLayoutManager(this, 2)
        controller.spanCount = 2
        layoutManager.spanSizeLookup = controller.spanSizeLookup
        epoxyMainActivity.apply {
            setLayoutManager(layoutManager)
            setController(controller)
        }
        //TODO: Just mock, remove this after you load pictures from Firebase and send them to controller

        GlobalScope.launch(Dispatchers.Main) {
            val imagePaths = storageInteractor.getImagePaths()

            val mainUI = MainUI(
                imagePaths.mapIndexed { index, path ->
                    Image(index, "", path)
                }
            )

            controller.setData(mainUI)
        }


        ivCapture.setOnClickListener {
            startActivityForResult(
                Intent(this, CameraActivity::class.java),
                REQUEST_CODE_IMAGE_CAPTURE
            )
        }
        ivGallery.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Select Picture"),
                REQUEST_CODE_GALLERY
            )

        }
        ivMenu.setOnClickListener {
            drawerRoot.openDrawer(GravityCompat.START)
        }

        logOut.setOnClickListener {
            authInteractor.logOut()

            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun onItemClick(itemDownloadUrl: StorageReference) {
        GlobalScope.launch {
            val image = storageInteractor.downloadImage(itemDownloadUrl)

            println(image?.byteCount)
        }
    }


    override fun onBackPressed() {
        if (drawerRoot.isDrawerOpen(GravityCompat.START)) {
            drawerRoot.closeDrawers()
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(KEY_PATH) ?: ""

            if (filePath.isNotBlank()) {
                startActivity(UploadActivity.newIntent(this, filePath, false))
            }
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data?.data ?: return

            startActivity(UploadActivity.newIntent(this, selectedImage.toString(), true))
        }
    }
}

class MainActivityController(private inline val onItemClick: (StorageReference) -> Unit) :
    TypedEpoxyController<MainUI>() {
    override fun buildModels(data: MainUI) {
        data.images.forEach { image ->
            imageItemModelHolder {
                id(image.imageId)
                imageTitle(image.imageTitle)
                imageURL(image.imageURL)
                listener { itemDownloadUrl -> onItemClick(itemDownloadUrl) }
            }
        }
    }
}
