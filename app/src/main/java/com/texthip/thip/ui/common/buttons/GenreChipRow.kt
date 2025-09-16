package com.texthip.thip.ui.common.buttons

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GenreChipRow(
    modifier: Modifier = Modifier.width(4.dp),
    genres: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    
    if (screenWidthDp < 360) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                genres.take(3).forEachIndexed { idx, genre ->
                    OptionChipButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)),
                        text = genre,
                        isFilled = true,
                        isSelected = selectedIndex == idx,
                        onClick = {
                            if (selectedIndex == idx) {
                                onSelect(-1)
                            } else {
                                onSelect(idx)
                            }
                        }
                    )
                    if (idx < 2) {
                        Spacer(modifier = modifier)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                genres.drop(3).forEachIndexed { relativeIdx, genre ->
                    val idx = relativeIdx + 3
                    OptionChipButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)),
                        text = genre,
                        isFilled = true,
                        isSelected = selectedIndex == idx,
                        onClick = {
                            if (selectedIndex == idx) {
                                onSelect(-1)
                            } else {
                                onSelect(idx)
                            }
                        }
                    )
                    if (relativeIdx < genres.drop(3).size - 1) {
                        Spacer(modifier = modifier)
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement
        ) {
            genres.forEachIndexed { idx, genre ->
                OptionChipButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp)),
                    text = genre,
                    isFilled = true,
                    isSelected = selectedIndex == idx,
                    onClick = {
                        if (selectedIndex == idx) {
                            onSelect(-1)
                        } else {
                            onSelect(idx)
                        }
                    }
                )
                if (idx < genres.size - 1) {
                    Spacer(modifier = modifier)
                }
            }
        }
    }
}

@Preview(name = "Normal Screen (>=360dp)", widthDp = 400)
@Composable
fun PreviewGenreChipRowNormal() {
    ThipTheme {
        GenreChipRow(
            genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술"),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}

@Preview(name = "Small Screen (<360dp)", widthDp = 320)
@Composable
fun PreviewGenreChipRowSmall() {
    ThipTheme {
        GenreChipRow(
            genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술"),
            selectedIndex = 2,
            onSelect = {}
        )
    }
}
