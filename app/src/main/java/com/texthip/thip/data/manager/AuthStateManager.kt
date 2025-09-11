package com.texthip.thip.data.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStateManager @Inject constructor() {
    private val _tokenExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val tokenExpiredEvent = _tokenExpiredEvent.asSharedFlow()
    
    fun triggerTokenExpired() {
        _tokenExpiredEvent.tryEmit(Unit)
    }
}