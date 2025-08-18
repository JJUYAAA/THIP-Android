package com.texthip.thip.utils.auth

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

fun getKakaoKeyHash(context: Context) {
    try {
        val info = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures!!) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            Log.d("KeyHash", keyHash)
        }
    } catch (e: Exception) {
        Log.e("KeyHash", "Exception", e)
    }
}