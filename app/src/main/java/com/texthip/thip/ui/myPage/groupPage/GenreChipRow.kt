package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.ui.common.buttons.OptionChipButton

@Composable
fun GenreChipRow(
    genres: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        genres.forEachIndexed { idx, genre ->
            OptionChipButton(
                text = genre,
                isFilled = true,
                isSelected = selectedIndex == idx,
                onClick = { onSelect(idx) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360)
@Composable
fun PreviewGenreChipRow() {
    GenreChipRow(
        genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술"),
        selectedIndex = 0,
        onSelect = {}
    )
}