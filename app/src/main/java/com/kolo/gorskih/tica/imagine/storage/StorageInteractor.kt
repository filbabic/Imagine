package com.kolo.gorskih.tica.imagine.storage

import android.graphics.Bitmap
import android.net.Uri

/**
 * Handles all access to storage - upload and download of images.
 */
interface StorageInteractor {

    suspend fun uploadImage(file: Uri): Boolean

    suspend fun downloadImage(imagePath: String): Bitmap?
}