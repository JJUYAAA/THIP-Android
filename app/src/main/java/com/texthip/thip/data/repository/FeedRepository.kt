package com.texthip.thip.data.repository

import android.content.Context
import android.net.Uri
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.feed.request.CreateFeedRequest
import com.texthip.thip.data.model.feed.response.CreateFeedResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import com.texthip.thip.data.model.feeds.response.AllFeedResponse
import com.texthip.thip.data.model.feeds.response.MyFeedResponse
import com.texthip.thip.data.service.FeedService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedService: FeedService,
    @param:ApplicationContext private val context: Context,
    private val json: Json
) {

    /** 피드 작성에 필요한 카테고리 및 태그 목록 조회 */
    suspend fun getFeedWriteInfo(): Result<FeedWriteInfoResponse?> = runCatching {
        val response = feedService.getFeedWriteInfo()
            .handleBaseResponse()
            .getOrThrow()
        
        // 카테고리 순서 조정
        val orderedCategories = response?.categoryList?.sortedBy { category ->
            when (category.category) {
                "문학" -> 0
                "과학·IT" -> 1
                "사회과학" -> 2
                "인문학" -> 3
                "예술" -> 4
                else -> 999
            }
        } ?: emptyList()
        
        response?.copy(categoryList = orderedCategories)
    }

    /** 피드 생성 */
    suspend fun createFeed(
        isbn: String,
        contentBody: String,
        isPublic: Boolean,
        tagList: List<String>,
        imageUris: List<Uri>
    ): Result<CreateFeedResponse?> = runCatching {
        val request = CreateFeedRequest(
            isbn = isbn,
            contentBody = contentBody,
            isPublic = isPublic,
            tagList = tagList
        )

        // JSON 요청 부분을 RequestBody로 변환
        val requestJson = json.encodeToString(CreateFeedRequest.serializer(), request)
        val requestBody = requestJson.toRequestBody("application/json".toMediaType())

        // 임시 파일 목록 추적
        val tempFiles = mutableListOf<File>()
        
        // 이미지 파일들을 MultipartBody.Part로 변환
        val imageParts = if (imageUris.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                imageUris.mapNotNull { uri ->
                    runCatching {
                        uriToMultipartBodyPart(uri, "images", tempFiles)
                    }.getOrNull()
                }
            }
        } else {
            null
        }

        try {
            feedService.createFeed(requestBody, imageParts)
                .handleBaseResponse()
                .getOrThrow()
        } finally {
            // 임시 파일들 정리
            cleanupTempFiles(tempFiles)
        }
    }

    private fun uriToMultipartBodyPart(uri: Uri, paramName: String, tempFiles: MutableList<File>): MultipartBody.Part? {
        return runCatching {
            // MIME 타입 확인
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val extension = when (mimeType) {
                "image/png" -> "png"
                "image/gif" -> "gif"
                "image/jpeg", "image/jpg" -> "jpg"
                else -> "jpg" // 기본값
            }
            
            // 파일명 생성
            val fileName = "feed_image_${System.currentTimeMillis()}.$extension"
            val tempFile = File(context.cacheDir, fileName)
            
            // 임시 파일 목록에 추가
            tempFiles.add(tempFile)
            
            // InputStream을 use 블록으로 안전하게 관리
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw IllegalStateException("Failed to open input stream for URI: $uri")
            
            // MultipartBody.Part 생성
            val requestFile = tempFile.asRequestBody(mimeType.toMediaType())
            MultipartBody.Part.createFormData(paramName, fileName, requestFile)
        }.onFailure { e ->
            e.printStackTrace()
        }.getOrNull()
    }

    /** 전체 피드 목록 조회 */
    suspend fun getAllFeeds(cursor: String? = null): Result<AllFeedResponse?> = runCatching {
        feedService.getAllFeeds(cursor)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 내 피드 목록 조회 */
    suspend fun getMyFeeds(cursor: String? = null): Result<MyFeedResponse?> = runCatching {
        feedService.getMyFeeds(cursor)
            .handleBaseResponse()
            .getOrThrow()
    }

    /** 임시 파일들을 정리하는 함수 */
    private fun cleanupTempFiles(tempFiles: List<File>) {
        tempFiles.forEach { file ->
            runCatching {
                if (file.exists()) {
                    file.delete()
                }
            }.onFailure { e ->
                e.printStackTrace()
            }
        }
    }
}