package com.kolo.gorskih.tica.imagine.interaction

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Implementation for [StorageInteractor].
 */
class StorageInteractorImpl(
    private val baseStorage: StorageReference,
    private val firebaseAuth: FirebaseAuth,
    private val context: Context
) : StorageInteractor {

    override suspend fun downloadImage(imagePath: String): Bitmap? = withContext(Dispatchers.IO) {
        val file = File(
            context.externalMediaDirs.first(),
            imagePath
        )

        val fileDownloadTask = baseStorage.child(imagePath)
            .getFile(file)

        try {
            Tasks.await(fileDownloadTask)
            // once downloaded, pull out as a bitmap

            BitmapFactory.decodeFile(imagePath)
        } catch (error: Throwable) {
            error.printStackTrace()
            null
        }
    }

    override suspend fun uploadImage(file: Uri): Boolean = withContext(Dispatchers.Default) {
        val lastSegment = file.lastPathSegment ?: return@withContext false
        val userId = firebaseAuth.currentUser?.uid ?: "unknown"

        val task = baseStorage
            .child(userId)
            .child(lastSegment)
            .putFile(file)

        try {
            val result = Tasks.await(task)

            result?.task?.isSuccessful ?: false
        } catch (throwable: Throwable) {
            false
        }
    }
}