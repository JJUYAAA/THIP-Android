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
    private val hardcodedToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTc1NDI4MjMzNiwiZXhwIjoxNzU2ODc0MzM2fQ.NG_xDSdh8A6egIX2EAFtsqDO4lmFphTzqgzHC-r8eXY"
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Authorization 헤더 추가
        val requestWithAuth = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()
        
        return chain.proceed(requestWithAuth)
    }
}