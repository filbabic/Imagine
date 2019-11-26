package com.kolo.gorskih.tica.imagine.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.Point
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kolo.gorskih.tica.imagine.R
import com.kolo.gorskih.tica.imagine.ui.main.KEY_PATH
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.concurrent.Executors

/**
 * Displays the camera view finder, and enables image capturing.
 */

private const val REQUEST_CODE_CAMERA = 505
private val requiredPermissions = arrayOf(Manifest.permission.CAMERA)

class CameraActivity : AppCompatActivity() {

    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (allPermissionsAllowed()) {
            viewFinder.post { startCamera() }
        } else {
            requestRequiredPermissions()
        }

        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }
    }

    private fun requestRequiredPermissions() {
        ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_CAMERA)
    }


    private fun startCamera() {
        val currentSize = Point()
        window?.windowManager?.defaultDisplay?.getSize(currentSize)

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(currentSize.x, currentSize.y))
        }.build()

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply { setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY) }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)

        captureButton.setOnClickListener {
            val file = File(
                externalMediaDirs.first(),
                "${System.currentTimeMillis()}.jpg"
            ).apply {

            }

            imageCapture.takePicture(file, executor,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        imageCaptureError: ImageCapture.ImageCaptureError,
                        message: String,
                        exc: Throwable?
                    ) {
                        val msg = "Photo capture failed: $message"

                        viewFinder.post {
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onImageSaved(file: File) {
                        val msg = "Photo capture succeeded: ${file.absolutePath}"

                        viewFinder.post {
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                        }
                        setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(KEY_PATH, file.absolutePath)
                        })
                        finish()
                    }
                })
        }

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (allPermissionsAllowed()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsAllowed(): Boolean {
        return requiredPermissions.all { requiredPermission ->
            ContextCompat.checkSelfPermission(
                this,
                requiredPermission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}