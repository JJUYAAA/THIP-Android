package com.texthip.thip.data.manager

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreManager @Inject constructor() {
    
    fun getGenres(): List<Genre> {
        return Genre.entries
    }
    
    fun mapGenreToApiCategory(genre: Genre): String {
        return genre.networkApiCategory
    }
    
    fun getDefaultGenre(): Genre {
        return Genre.getDefault()
    }
    
    fun getGenreByDisplayKey(displayKey: String): Genre? {
        return Genre.fromDisplayKey(displayKey)
    }
}