package com.pulse.manager.core.extensions

import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.getMultipartBody(fileName: String): MultipartBody.Part {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(this.absolutePath)
    val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
    val fileRequestBody = this.asRequestBody(fileType?.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fileName, name, fileRequestBody)
}