package com.texthip.thip.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "thip_tokens")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        //토큰저장에 사용되는 키
        private val APP_TOKEN_KEY = stringPreferencesKey("app_token")
        private val TEMP_TOKEN_KEY = stringPreferencesKey("temp_token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    //토큰 저장
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[APP_TOKEN_KEY] = token
        }
    }

    //저장된 토큰을 Flow 형태로 불러옴
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[APP_TOKEN_KEY]
        }
    }

    //저장된 토큰 삭제 (로그아웃 시?)
    suspend fun deleteToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(APP_TOKEN_KEY)
        }
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