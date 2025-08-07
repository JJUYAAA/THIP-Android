package com.texthip.thip.data.manager

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataManager @Inject constructor() {
    
    private var cachedUserName: String? = null
    
    fun getUserName(): String {
        return cachedUserName ?: "사용자"
    }
    
    fun cacheUserName(name: String) {
        cachedUserName = name
    }
    
    fun clearUserData() {
        cachedUserName = null
    }
    
    fun hasUserName(): Boolean {
        return cachedUserName != null
    }
}