package com.kolo.gorskih.tica.imagine.interaction

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kolo.gorskih.tica.imagine.common.ImageStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation for [StorageInteractor].
 */
class StorageInteractorImpl(
    private val firebaseStorage: FirebaseStorage,
    private val database: FirebaseDatabase,
    private val authInteractor: AuthInteractor,
    private val context: Context
) : StorageInteractor {

    override suspend fun downloadImage(imagePath: StorageReference): Pair<String?, Bitmap?> =
        withContext(Dispatchers.IO) {
            val reference = firebaseStorage.getReferenceFromUrl(imagePath.toString())

            val file = File(
                context.externalMediaDirs.first(),
                "${reference.name}.jpg"
            )

            val fileDownloadTask = reference.getFile(file)

            try {
                Tasks.await(fileDownloadTask)
                // once downloaded, pull out as a bitmap

                file.absolutePath to BitmapFactory.decodeFile(file.absolutePath)
            } catch (error: Throwable) {
                error.printStackTrace()

                null to null
            }
        }

    override suspend fun uploadImage(file: Uri): Boolean = withContext(Dispatchers.IO) {
        val lastSegment = file.lastPathSegment ?: return@withContext false
        val userId = authInteractor.getUserId()

        val location = firebaseStorage
            .reference
            .child(userId)
            .child(lastSegment)

        val task = location.putFile(file)

        try {
            val result = Tasks.await(task)
            val downloadUrl = location.toString()

            Tasks.await(
                database
                    .reference
                    .child(authInteractor.getUserId())
                    .push()
                    .setValue(downloadUrl)
            )

            result?.task?.isSuccessful == true
        } catch (throwable: Throwable) {
            false
        }
    }

    override suspend fun getImagePaths(): List<StorageReference> =
        suspendCoroutine { continuation ->

            database
                .reference
                .child(authInteractor.getUserId())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {
                        continuation.resumeWithException(databaseError.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot
                            .children
                            .mapNotNull { it.getValue(String::class.java) }
                            .map { url -> firebaseStorage.getReferenceFromUrl(url) }

                        continuation.resume(children)
                    }
                })
        }

    override suspend fun getDownloadedImageFromPath(imagePath: String): Bitmap? {
        return try {
            val file = File(imagePath)

            BitmapFactory.decodeFile(file.absolutePath)
        } catch (error: Throwable) {
            null
        }
    }

    override suspend fun saveLocalImageToFile(bitmap: Bitmap): File {
        val path = ImageStorageManager.saveToInternalStorage(
            context,
            bitmap,
            "${System.currentTimeMillis()}"
        )

        return File(path)
    }
}