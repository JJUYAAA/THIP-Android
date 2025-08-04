package com.texthip.thip.data.di

import com.texthip.thip.data.model.service.GroupService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    
    @Provides
    @Singleton
    fun provideGroupService(retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }
}