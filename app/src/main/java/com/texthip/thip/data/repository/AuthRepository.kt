package com.texthip.thip.data.repository

import android.content.Context
import com.kakao.sdk.user.UserApiClient
import com.texthip.thip.data.model.auth.request.AuthRequest
import com.texthip.thip.data.model.auth.response.AuthResponse
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.service.AuthService
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthService
) {

    suspend fun loginWithKakao(context: Context): Result<AuthResponse?> {
        return runCatching {
            //카카오SDK를 통해 사용자 고유 ID 가져옴
            val kakaoUserId = getKakaoUserId(context)

            //신규/기존 유저인지 확인 요청
             val request = AuthRequest(oauth2Id = "kakao_$kakaoUserId")
            authService.checkNewUser(request)
                .handleBaseResponse()
                .getOrThrow()
        }
    }

    //카카오 SDK 로그인 수행 -> 유저ID를 반환하는 suspend 메서드
    private suspend fun getKakaoUserId(context: Context): Long = suspendCancellableCoroutine { continuation ->
        // 카카오톡 설치 여부에 따라 로그인 방식 결정
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    // 로그인 성공 시 사용자 정보 요청
                    fetchUserInfo(continuation)
                }
            }
        } else {
            // 카카오계정으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    // 로그인 성공 시 사용자 정보 요청
                    fetchUserInfo(continuation)
                }
            }
        }
    }

    //사용자 정보를 가져오는 중복 코드를 별도의 함수로 분리함
    private fun fetchUserInfo(continuation: CancellableContinuation<Long>) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (user?.id != null) {
                continuation.resume(user.id!!)
            } else {
                continuation.resumeWithException(IllegalStateException("Kakao User ID is null"))
            }
        }
    }
}