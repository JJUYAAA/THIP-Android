package com.texthip.thip.data.di

import com.texthip.thip.data.service.BookService
import com.texthip.thip.data.service.GroupService
import com.texthip.thip.data.service.RoomsService
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

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }
    @Provides
    @Singleton
    fun providesRoomsService(retrofit: Retrofit): RoomsService =
        retrofit.create(RoomsService::class.java)
}