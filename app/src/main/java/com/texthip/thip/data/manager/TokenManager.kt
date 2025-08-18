package com.texthip.thip.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "thip_tokens")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // 저장할 데이터의 Key 정의
    companion object {
        private val TEMP_TOKEN_KEY = stringPreferencesKey("temp_token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    // 임시 토큰 저장
    suspend fun saveTempToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TEMP_TOKEN_KEY] = token
        }
    }

    // 임시 토큰 읽기
    suspend fun getTempToken(): String? {
        return context.dataStore.data.map { prefs ->
            prefs[TEMP_TOKEN_KEY]
        }.first() // Flow에서 첫 번째 값을 한번만 읽어옴
    }

    // 정식 토큰들(Access, Refresh) 저장
    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    // Access Token 읽기 (Flow로 제공하여 토큰 변화를 감지할 수 있게 함)
    fun getAccessToken(): kotlinx.coroutines.flow.Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    // 모든 토큰 삭제 (로그아웃 시)
    suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}