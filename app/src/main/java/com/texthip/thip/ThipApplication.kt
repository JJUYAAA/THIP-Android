package com.texthip.thip

import com.kakao.sdk.common.KakaoSdk
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.texthip.thip.BuildConfig


@HiltAndroidApp
class ThipApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 카카오 SDK 초기화
        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
    }
}