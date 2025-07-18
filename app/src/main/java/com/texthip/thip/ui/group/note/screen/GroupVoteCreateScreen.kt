package com.texthip.thip.ui.group.note.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.common.modal.ArrowPosition
import com.texthip.thip.ui.common.modal.PopupModal
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.note.component.PageInputSection
import com.texthip.thip.ui.group.note.component.VoteInputSection
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupVoteCreateScreen() {
    var pageText by rememberSaveable { mutableStateOf("") }
    var isGeneralReview by rememberSaveable { mutableStateOf(false) }

    var title by rememberSaveable { mutableStateOf("") }
    var options by rememberSaveable { mutableStateOf(mutableListOf("", "")) }

    val density = LocalDensity.current
    var showTooltip by rememberSaveable { mutableStateOf(false) }

    // Tooltip 위치 측정용 state
    val iconCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    var isEligible by rememberSaveable { mutableStateOf(false) } // TODO: 서버 데이터?

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            InputTopAppBar(
                title = stringResource(R.string.create_vote),
                isRightButtonEnabled = false,
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
                    isEligible = isEligible,
                    bookTotalPage = 600,
                    onInfoClick = { showTooltip = true },
                    onInfoPositionCaptured = { iconCoordinates.value = it }
                )

                VoteInputSection(
                    title = title,
                    onTitleChange = { title = it },
                    options = options,
                    onOptionChange = { index, newText ->
                        options = options.toMutableList().also { it[index] = newText }
                    },
                    onAddOption = {
                        if (options.size < 5) {
                            options = options.toMutableList().also { it.add("") }
                        }
                    },
                    onRemoveOption = { index ->
                        options = options.toMutableList().also { it.removeAt(index) }
                    }
                )
            }
        }


        if (showTooltip && iconCoordinates.value != null) {
            val yOffsetDp = with(density) {
                iconCoordinates.value!!.positionInRoot().y.toDp() + 32.dp
            }

            Box(
                modifier = Modifier
                    .absoluteOffset(y = yOffsetDp)
                    .padding(horizontal = 20.dp)
                    .zIndex(1f)
            ) {
                PopupModal(
                    text = stringResource(R.string.condition_of_general_review),
                    arrowPosition = ArrowPosition.RIGHT,
                    isEligible = isEligible,
                    onClose = { showTooltip = false }
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupVoteCreateScreenPreview() {
    ThipTheme {
        GroupVoteCreateScreen()
    }
}