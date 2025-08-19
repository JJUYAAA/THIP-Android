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