package com.texthip.thip.utils.auth

import com.texthip.thip.data.manager.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        // 1. 정식 토큰을 먼저 확인합니다.
        val token = runBlocking { tokenManager.getTokenOnce() }
        // 2. 정식 토큰이 없으면, 임시 토큰을 확인합니다.
        val tempToken = runBlocking { tokenManager.getTempTokenOnce() }

        // 보낼 토큰을 결정합니다 (정식 토큰 우선).
        val tokenToSend = token ?: tempToken

        val newRequest = original.newBuilder().apply {
            tokenToSend?.let { addHeader("Authorization", "Bearer $it") }
        }.build()

        return chain.proceed(newRequest)
    }
}
