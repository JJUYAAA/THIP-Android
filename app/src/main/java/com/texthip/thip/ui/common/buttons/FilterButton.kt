package com.texthip.thip.ui.common.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption,
                color = colors.Grey01,
                style = typography.menu_m500_s14_h24
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_downmore),
                contentDescription = "Dropdown",
                tint = colors.Grey02,
                modifier = Modifier.size(24.dp)
            )

        }

        // 드롭다운 애니메이션 박스
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(144.dp)
                    .border(width = 1.dp, color = colors.Grey01, shape = RoundedCornerShape(16.dp))
                    .background(color = colors.Black, shape = RoundedCornerShape(16.dp))
                    .padding(vertical = 20.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                options.forEach { option ->
                    val isSelected = option == selectedOption
                    Text(
                        text = option,
                        color = if (isSelected) colors.White else colors.Grey02,
                        style = typography.feedcopy_r400_s14_h20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                expanded = false
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FilterButtonPreview() {
    var selected by remember { mutableStateOf("인기순") }
    val options = listOf("인기순", "정확도순")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FilterButton(
            selectedOption = selected,
            options = options,
            onOptionSelected = { selected = it }
        )
    }
}