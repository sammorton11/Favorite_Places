package com.samm.imagesaver.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    companion object {
        fun getImageUri(imageBitmap: Bitmap, context: Context?): Uri {
            val bytes = ByteArrayOutputStream()
            val resolver = context?.contentResolver
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val resized = Bitmap.createScaledBitmap(imageBitmap, 900, 900, true)
            val path =
                MediaStore.Images.Media.insertImage(resolver, resized, "Title", null)
            return Uri.parse(path)
        }
    }
}