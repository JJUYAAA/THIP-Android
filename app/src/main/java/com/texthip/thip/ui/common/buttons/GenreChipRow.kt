package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GenreChipRow(
    modifier: Modifier = Modifier.width(4.dp),
    genres: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        genres.forEachIndexed { idx, genre ->
            OptionChipButton(
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

@Preview()
@Composable
fun PreviewGenreChipRow() {
    ThipTheme {
        GenreChipRow(
            genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술"),
            selectedIndex = 0,
            onSelect = {}
        )
    }
}
