package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.forms.BookPageTextField
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun PageInputSection(
    title: String = stringResource(R.string.page_to_record),
    pageText: String,
    onPageTextChange: (String) -> Unit,
    isGeneralReview: Boolean,
    onGeneralReviewToggle: (Boolean) -> Unit,
    bookTotalPage: Int,
    isEligible: Boolean,
    onInfoClick: () -> Unit,
    onInfoPositionCaptured: (LayoutCoordinates) -> Unit
) {
    val allRangeText = stringResource(R.string.all_range)
    val isError = remember(pageText, bookTotalPage, isGeneralReview) {
        if (isGeneralReview) {
            false
        } else {
            pageText.toIntOrNull()?.let { it > bookTotalPage } ?: false
        }
    }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )

        Box(
            modifier = Modifier.height(90.dp),
        ) {
            BookPageTextField(
                modifier = Modifier.fillMaxWidth(),
                bookTotalPage = bookTotalPage,
                text = if (isGeneralReview) allRangeText else pageText,
                onValueChange = {
                    if (!isGeneralReview) onPageTextChange(it)
                },
                enabled = !isGeneralReview,
                isError = isError
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_information),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .onGloballyPositioned { coordinates ->
                                onInfoPositionCaptured(coordinates)
                            }
                            .clickable { onInfoClick() },
                        tint = colors.Grey02
                    )

                    Text(
                        text = stringResource(R.string.general_review),
                        style = typography.info_r400_s12,
                        color = colors.Grey
                    )
                }

                ToggleSwitchButton(
                    isChecked = isGeneralReview,
                    onToggleChange = { checked ->
                        onGeneralReviewToggle(checked)
                        onPageTextChange(if (checked) allRangeText else "")
                    },
                    enabled = isEligible
                )
            }
        }
    }
}

@Preview
@Composable
private fun PageInputSectionPreview() {
    var pageText by rememberSaveable { mutableStateOf("") }
    var isGeneralReview by rememberSaveable { mutableStateOf(false) }

    PageInputSection(
        pageText = pageText,
        onPageTextChange = { pageText = it },
        isGeneralReview = isGeneralReview,
        onGeneralReviewToggle = { isGeneralReview = it },
        bookTotalPage = 500,
        isEligible = true,
        onInfoClick = {},
        onInfoPositionCaptured = {}
    )
}