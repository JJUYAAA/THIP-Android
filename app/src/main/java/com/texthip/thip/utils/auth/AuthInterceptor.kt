package com.texthip.thip.utils.auth

import com.texthip.thip.data.manager.AuthStateManager
import com.texthip.thip.data.manager.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authStateManager: AuthStateManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        val token = runBlocking { tokenManager.getTokenOnce() }
        val tempToken = runBlocking { tokenManager.getTempTokenOnce() }

        val tokenToSend = token ?: tempToken

        val newRequest = original.newBuilder().apply {
            tokenToSend?.let { addHeader("Authorization", "Bearer $it") }
        }.build()

        val response = chain.proceed(newRequest)
        
        // 401 응답 처리
        if (response.code == 401) {
            runBlocking {
                tokenManager.clearTokens()
                authStateManager.triggerTokenExpired()
            }
        }

        return response
    }
}
