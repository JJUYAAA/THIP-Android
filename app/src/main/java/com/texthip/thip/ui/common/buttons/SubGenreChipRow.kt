package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun SubGenreChipGrid(
    subGenres: List<String>,
    selectedGenres: List<String>,
    onGenreToggle: (String) -> Unit,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, horizontalAlignment),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val isLimitReached = selectedGenres.size >= 5

        subGenres.forEach { genre ->
            val isSelected = selectedGenres.contains(genre)
            val isEnabled = isSelected || !isLimitReached

            OptionChipButton(
                text = genre,
                isSelected = isSelected,
                isFilled = false,
                enabled = isEnabled,
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