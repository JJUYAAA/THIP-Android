package com.texthip.thip.ui.group.note.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.forms.BookPageTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupNoteCreateScreen() {
    var pageText by rememberSaveable { mutableStateOf("") }
    var isGeneralReview by rememberSaveable { mutableStateOf(false) }

    val allRangeText = stringResource(R.string.all_range)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        InputTopAppBar(
            title = stringResource(R.string.write_record),
            isRightButtonEnabled = false,
            onLeftClick = { /* 뒤로가기 동작 */ },
            onRightClick = { /* 완료 동작 */ }
        )

        Column(
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(R.string.page_to_record),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )

                Box(
                    modifier = Modifier.height(90.dp),
                ) {
                    BookPageTextField(
                        modifier = Modifier.fillMaxWidth(),
                        bookTotalPage = 500, // TODO: 서버 데이터로 수정
                        text = if (isGeneralReview) stringResource(R.string.all_range) else pageText,
                        onValueChange = {
                            if (!isGeneralReview) pageText = it
                        },
                        enabled = !isGeneralReview
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
                                tint = colors.Grey02
                            )

                            Text(
                                text = stringResource(R.string.general_review),
                                style = typography.info_r400_s12,
                                color = colors.Grey
                            )
                        }

                        ToggleSwitchButton(
                            isChecked = isGeneralReview
                        ) { checked ->
                            isGeneralReview = checked
                            pageText = if (checked) {
                                allRangeText
                            } else {
                                ""
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupNoteCreateScreenPreview() {
    ThipTheme {
        GroupNoteCreateScreen()
    }
}