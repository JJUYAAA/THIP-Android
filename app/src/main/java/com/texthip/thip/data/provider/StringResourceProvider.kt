package com.texthip.thip.data.provider

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringResourceProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
    
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}