package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FilterChipButton
import com.texthip.thip.ui.common.buttons.FormButton
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FilterHeaderSection(
    firstPage: String,
    lastPage: String,
    isTotalSelected: Boolean,
    totalEnabled: Boolean = true,
    onFirstPageChange: (String) -> Unit,
    onLastPageChange: (String) -> Unit,
    onTotalToggle: () -> Unit,
) {
    var isPageInputVisible by rememberSaveable { mutableStateOf(false) }
    val isPageFiltered = firstPage.isNotBlank() || lastPage.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterChipButton(
                text = stringResource(R.string.view_by_page),
                isSelected = isPageFiltered,
                onClick = {
                    isPageInputVisible = !isPageInputVisible
                },
                onCloseClick = {
                    isPageInputVisible = true
                }
            )

            OptionChipButton(
                modifier = Modifier.height(36.dp),
                text = stringResource(R.string.view_by_all),
                isFilled = true,
                isSelected = isTotalSelected,
                enabled = totalEnabled,
                textStyle = typography.menu_r400_s14_h24,
                onClick = onTotalToggle
            )
        }

        if (isPageInputVisible) {
            FormButton(
                firstPage = firstPage,
                onFirstPageChange = onFirstPageChange,
                lastPage = lastPage,
                onLastPageChange = onLastPageChange,
                onFinishClick = {
                    isPageInputVisible = false
                }
            )
        }
    }
}

@Preview
@Composable
private fun FilterHeaderSectionPreview() {
    var firstPage by rememberSaveable { mutableStateOf("") }
    var lastPage by rememberSaveable { mutableStateOf("") }
    var isTotalSelected by rememberSaveable { mutableStateOf(false) }

    FilterHeaderSection(
        firstPage = firstPage,
        lastPage = lastPage,
        isTotalSelected = isTotalSelected,
        onFirstPageChange = { firstPage = it },
        onLastPageChange = { lastPage = it },
        onTotalToggle = { isTotalSelected = !isTotalSelected },
        totalEnabled = true
    )
}