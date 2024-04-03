package com.hifi.redeal.memo.datastore

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveImages(context: Context, imageUris: List<Uri>):List<String> {
    val savedImagePaths = mutableListOf<String>()

    val imagesDirectory = File(context.filesDir, "images")
    if (!imagesDirectory.exists()) {
        imagesDirectory.mkdirs()
    }

    for (imageUri in imageUris) {
        val imagePath = saveImageFile(context, imageUri)
        if (imagePath != null) {
            savedImagePaths.add(imagePath)
        }
    }

    return savedImagePaths
}

private fun saveImageFile(context: Context, imageUri: Uri): String? {
    val inputStream = context.contentResolver.openInputStream(imageUri)
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
        return file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        bitmap?.recycle()
    }

    return null
}