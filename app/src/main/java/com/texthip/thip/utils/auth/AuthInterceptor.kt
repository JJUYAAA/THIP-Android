package com.texthip.thip.utils.auth

import com.texthip.thip.data.manager.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1. 요청에 이미 Authorization 헤더가 있는지 확인합니다.
        if (originalRequest.header("Authorization") != null) {
            // 이미 헤더가 있다면, 아무 작업도 하지 않고 그대로 보냅니다.
            return chain.proceed(originalRequest)
        }

        val appToken = runBlocking { tokenManager.getToken().first() }
        val tempToken = runBlocking { tokenManager.getTempToken() }
        val tokenToSend = appToken ?: tempToken
        val requestBuilder = chain.request().newBuilder()

        if (!tokenToSend.isNullOrBlank()) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $tokenToSend"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}