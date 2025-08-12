package com.texthip.thip.utils.rooms

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.texthip.thip.R
import com.texthip.thip.data.manager.Genre

/**
 * Genre enum을 UI에서 사용하기 위한 확장 함수들
 */

@Composable
fun Genre.toDisplayString(): String {
    return when (this) {
        Genre.LITERATURE -> stringResource(R.string.literature)
        Genre.SCIENCE_IT -> stringResource(R.string.science_it)
        Genre.SOCIAL_SCIENCE -> stringResource(R.string.social_science)
        Genre.HUMANITIES -> stringResource(R.string.humanities)
        Genre.ART -> stringResource(R.string.art)
    }
}

@Composable
fun List<Genre>.toDisplayStrings(): List<String> {
    return this.map { it.toDisplayString() }
}