package com.kolo.gorskih.tica.imagine.interaction

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.StorageReference

/**
 * Handles all access to storage - upload and download of images.
 */
interface StorageInteractor {

    suspend fun uploadImage(file: Uri): Boolean

    suspend fun downloadImage(imagePath: StorageReference): Bitmap?

    suspend fun getImagePaths(): List<StorageReference>
}