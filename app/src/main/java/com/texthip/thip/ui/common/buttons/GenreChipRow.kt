package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp)), // 버튼 모양에 맞게 클리핑
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
