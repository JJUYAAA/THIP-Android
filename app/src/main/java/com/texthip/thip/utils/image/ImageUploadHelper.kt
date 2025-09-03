package com.texthip.thip.utils.image

import android.content.Context
import android.net.Uri
import com.texthip.thip.data.model.feed.request.ImageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploadHelper @Inject constructor(
    private val context: Context
) {
    
    private val s3Client = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
        )
        .build()
    
    suspend fun uploadImageToS3(
        uri: Uri,
        presignedUrl: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: throw IllegalStateException("Failed to open input stream for URI: $uri")

                val requestBody = tempFile.readBytes().toRequestBody(mimeType.toMediaType())
                val request = Request.Builder()
                    .url(presignedUrl)
                    .put(requestBody)
                    .build()

                val response = s3Client.newCall(request).execute()
                
                if (!response.isSuccessful) {
                    throw Exception("S3 upload failed: ${response.code} ${response.message}")
                }
            } finally {
                if (tempFile.exists()) {
                    tempFile.delete()
                }
            }
        }
    }
    
    fun getImageMetadata(uri: Uri): ImageMetadata? {
        return runCatching {
            val mimeType = context.contentResolver.getType(uri) ?: return null
            val extension = when (mimeType) {
                "image/png" -> "png"
                "image/jpeg", "image/jpg" -> "jpg"
                "image/gif" -> "gif"
                else -> return null
            }
            
            // 파일 크기 더 정확하게 계산
            val size = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                var totalBytes = 0L
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    totalBytes += bytesRead
                }
                totalBytes
            } ?: return null
            
            ImageMetadata(
                extension = extension,
                size = size
            )
        }.getOrNull()
    }
}