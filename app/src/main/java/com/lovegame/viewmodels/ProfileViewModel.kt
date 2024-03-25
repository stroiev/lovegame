package com.lovegame.viewmodels

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.lovegame.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    var selectedImages = mutableStateListOf<Uri?>()

    fun getUri(context: Context): Uri? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = "JPEG_$timeStamp.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        var uri: Uri? = null
        var stream: OutputStream? = null
        try {
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            if (uri == null) {
                throw IOException("Failed to create new MediaStore record.")
            }
            stream = context.contentResolver.openOutputStream(uri)
            if (stream == null) {
                throw IOException("Failed to get output stream.")
            }
        } catch (e: IOException) {
            if (uri != null) {
                context.contentResolver.delete(uri, null, null)
            }
            throw IOException(e)
        } finally {
            stream?.close()
        }
        return uri
    }

    fun deleteUri(uri: Uri, context: Context) {
        context.contentResolver.delete(uri, null, null)
    }

    fun signOut() = signOutUseCase.execute()
}