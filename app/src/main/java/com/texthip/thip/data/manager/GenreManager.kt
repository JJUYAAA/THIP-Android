package com.texthip.thip.data.manager

import android.content.Context
import com.texthip.thip.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    
    private val genres = listOf(
        context.getString(R.string.literature),
        context.getString(R.string.science_it),
        context.getString(R.string.social_science),
        context.getString(R.string.humanities),
        context.getString(R.string.art)
    )
    
    fun getGenres(): List<String> {
        return genres
    }
    
    fun mapGenreToApiCategory(genre: String): String {
        return when (genre) {
            context.getString(R.string.science_it) -> context.getString(R.string.api_genre_science_it)
            else -> genre
        }
    }
    
    fun getDefaultGenre(): String {
        return context.getString(R.string.literature)
    }
    
    fun isValidGenreIndex(index: Int): Boolean {
        return index >= 0 && index < genres.size
    }

}