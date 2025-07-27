package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun SubGenreChipGrid(
    subGenres: List<String>,
    selectedGenres: List<String>,
    onGenreToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        subGenres.forEach { genre ->
            OptionChipButton(
                text = genre,
                isSelected = selectedGenres.contains(genre),
                isFilled = false,
                onClick = { onGenreToggle(genre) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSubGenreChipGrid() {
    ThipTheme {
        val previewSubGenres = listOf(
            "태그1", "태그2", "태그3", "태그4", "태그5",
            "태그6", "태그7", "태그8", "태그9", "태그10"
        )
        val selectedGenres = remember { mutableStateListOf<String>() }

        SubGenreChipGrid(
            subGenres = previewSubGenres,
            selectedGenres = selectedGenres,
            onGenreToggle = { genre ->
                if (selectedGenres.contains(genre)) {
                    selectedGenres.remove(genre)
                } else {
                    selectedGenres.add(genre)
                }
            }
        )
    }
}