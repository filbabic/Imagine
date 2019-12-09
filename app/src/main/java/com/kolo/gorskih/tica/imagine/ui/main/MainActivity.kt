package com.kolo.gorskih.tica.imagine.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.Glide
import com.kolo.gorskih.tica.imagine.Image
import com.kolo.gorskih.tica.imagine.MainUI
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.ui.camera.CameraActivity
import com.kolo.gorskih.tica.imagine.ui.view_holders.imageItemModelHolder
import kotlinx.android.synthetic.main.activity_main.*


private const val REQUEST_CODE_IMAGE_CAPTURE = 51
const val KEY_PATH = "path"

class MainActivity : AppCompatActivity() {

    private val controller : MainActivityController = MainActivityController()

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
        val mainUI = MainUI(listOf(Image(1,"prva","https://www.frontal.ba/img/s/750x400/upload/images/slikesenka/;aruga.jpg"),Image(2,"druga s jako dugackim i lijepim nazivom jer smo mi samo lijepi i zgodni ","https://www.frontal.ba/img/s/750x400/upload/images/slikesenka/;aruga.jpg"),Image(3,"treca","https://www.frontal.ba/img/s/750x400/upload/images/slikesenka/;aruga.jpg"),Image(4,"Zadnja","https://www.frontal.ba/img/s/750x400/upload/images/slikesenka/;aruga.jpg")))
        controller.setData(mainUI)
        ivCapture.setOnClickListener {
            startActivityForResult(Intent(this, CameraActivity::class.java),REQUEST_CODE_IMAGE_CAPTURE)
        }
        ivGallery.setOnClickListener {
            //TODO: open gallery for selecting a picture
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(KEY_PATH) ?: ""

            if (filePath.isNotBlank()) {
                //TODO: define where this goes
                /*Glide.with(this)
                    .load(filePath)
                    .into(capturedImage)*/
            }
        }
    }
}

class MainActivityController : TypedEpoxyController<MainUI>(){
    override fun buildModels(data: MainUI) {
        for(i in 1..20)
        data.images.forEachIndexed { index, image ->
            imageItemModelHolder {
                id(image.imageId)
                imageTitle(image.imageTitle)
                imageURL(image.imageURL)
            }
        }
    }
}
