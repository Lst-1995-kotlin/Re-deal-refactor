package com.hifi.redeal.memo.datastore

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

fun saveImages(context: Context, imageUris: List<String>):List<String> {
    val savedImageUris = mutableListOf<String>()

    for (imageUri in imageUris) {
        val saveImageUri = saveImageFile(context, imageUri)
        if (saveImageUri != null) {
            savedImageUris.add(saveImageUri.toString())
        }
    }

    return savedImageUris
}

fun saveImageFile(context: Context, imageUri: String): Uri? {
    val inputStream = context.contentResolver.openInputStream(imageUri.toUri())
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val imagesDirectory = File(context.filesDir, "images")
    if (!imagesDirectory.exists()) {
        imagesDirectory.mkdirs()
    }

    val fileName = "image_${System.currentTimeMillis()}.jpg"
    val file = File(imagesDirectory, fileName)

    try {
        FileOutputStream(file).use { fos ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        }
        return file.toUri()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        bitmap?.recycle()
    }

    return null
}