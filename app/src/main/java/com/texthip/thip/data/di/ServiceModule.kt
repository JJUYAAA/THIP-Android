package com.texthip.thip.data.di

import com.texthip.thip.data.service.BookService
import com.texthip.thip.data.service.RecentSearchService
import com.texthip.thip.data.service.CommentsService
import com.texthip.thip.data.service.GroupService
import com.texthip.thip.data.service.RoomsService
import com.texthip.thip.data.service.UserService
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
    fun provideBookService(retrofit: Retrofit): BookService {
        return retrofit.create(BookService::class.java)
    }
    @Provides
    @Singleton
    fun providesRoomsService(retrofit: Retrofit): RoomsService =
        retrofit.create(RoomsService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecentSearchService(retrofit: Retrofit): RecentSearchService {
        return retrofit.create(RecentSearchService::class.java)
    }

    @Provides
    @Singleton
    fun providesCommentsService(retrofit: Retrofit): CommentsService =
        retrofit.create(CommentsService::class.java)
}