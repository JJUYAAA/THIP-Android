package com.texthip.thip.ui.group.note.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.note.component.OpinionInputSection
import com.texthip.thip.ui.group.note.component.PageInputSection
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupNoteCreateScreen() {
    var pageText by rememberSaveable { mutableStateOf("") }
    var isGeneralReview by rememberSaveable { mutableStateOf(false) }
    var opinionText by rememberSaveable { mutableStateOf("") }

    val isFormFilled = pageText.isNotBlank() && opinionText.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        InputTopAppBar(
            title = stringResource(R.string.write_record),
            isRightButtonEnabled = isFormFilled,
            onLeftClick = { /* 뒤로가기 동작 */ },
            onRightClick = { /* 완료 동작 */ }
        )

        Column(
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            PageInputSection(
                pageText = pageText,
                onPageTextChange = { pageText = it },
                isGeneralReview = isGeneralReview,
                onGeneralReviewToggle = { isGeneralReview = it },
                bookTotalPage = 500 // TODO: 서버 데이터로 변경
            )

            OpinionInputSection(
                text = opinionText,
                onTextChange = { opinionText = it }
            )

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