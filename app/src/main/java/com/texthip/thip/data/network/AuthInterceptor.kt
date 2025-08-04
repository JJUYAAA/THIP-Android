package com.texthip.thip.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    
    // TODO: 추후 실제 토큰 관리 시스템으로 대체 예정
    // - SharedPreferences나 DataStore에서 토큰 읽기
    // - 토큰 만료 시 자동 갱신
    // - 로그인/로그아웃 상태 관리
    private val hardcodedToken = "eyJhbGciOiJIUzI1NiJ9.eyJvYXV0aDJJZCI6Imtha2FvXzQzODE1MTU3MTEiLCJpYXQiOjE3NTQyOTQ2NTAsImV4cCI6MTc1Njg4NjY1MH0.ejCMjFf4mNEvSyliV6RQ4mPJPaGGgoKkuWX2rE0tu54"
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Authorization 헤더 추가
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()
        
        return chain.proceed(requestWithAuth)
    }
}