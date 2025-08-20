package com.texthip.thip.ui.group.note.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.ui.common.modal.ArrowPosition
import com.texthip.thip.ui.common.modal.PopupModal
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.note.component.PageInputSection
import com.texthip.thip.ui.group.note.component.VoteInputSection
import com.texthip.thip.ui.group.note.viewmodel.GroupVoteCreateEvent
import com.texthip.thip.ui.group.note.viewmodel.GroupVoteCreateUiState
import com.texthip.thip.ui.group.note.viewmodel.GroupVoteCreateViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.utils.rooms.advancedImePadding

@Composable
fun GroupVoteCreateScreen(
    roomId: Int,
    recentPage: Int,
    totalPage: Int,
    isOverviewPossible: Boolean,
    onBackClick: () -> Unit,
    onNavigateBackWithResult: () -> Unit,
    viewModel: GroupVoteCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initialize(roomId, recentPage, totalPage, isOverviewPossible)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBackWithResult()
        }
    }

    GroupVoteCreateContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun GroupVoteCreateContent(
    uiState: GroupVoteCreateUiState,
    onEvent: (GroupVoteCreateEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val density = LocalDensity.current
    var showTooltip by rememberSaveable { mutableStateOf(false) }
    val iconCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .advancedImePadding()
    ) {
        Column {
            InputTopAppBar(
                title = stringResource(R.string.create_vote),
                isRightButtonEnabled = uiState.isFormFilled,
                onLeftClick = onBackClick,
                onRightClick = { onEvent(GroupVoteCreateEvent.CreateVoteClicked) }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                PageInputSection(
                    pageText = uiState.pageText,
                    onPageTextChange = { onEvent(GroupVoteCreateEvent.PageChanged(it)) },
                    isGeneralReview = uiState.isGeneralReview,
                    onGeneralReviewToggle = { onEvent(GroupVoteCreateEvent.GeneralReviewToggled(it)) },
                    isEligible = uiState.isGeneralReviewEnabled,
                    bookTotalPage = uiState.bookTotalPage,
                    onInfoClick = { showTooltip = true },
                    onInfoPositionCaptured = { iconCoordinates.value = it }
                )

                VoteInputSection(
                    title = uiState.title,
                    onTitleChange = { onEvent(GroupVoteCreateEvent.TitleChanged(it)) },
                    options = uiState.options,
                    onOptionChange = { index, newText ->
                        onEvent(GroupVoteCreateEvent.OptionChanged(index, newText))
                    },
                    onAddOption = { onEvent(GroupVoteCreateEvent.AddOptionClicked) },
                    onRemoveOption = { index ->
                        onEvent(GroupVoteCreateEvent.RemoveOptionClicked(index))
                    }
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (showTooltip && iconCoordinates.value != null) {
            val yOffsetDp = with(density) {
                iconCoordinates.value!!.positionInRoot().y.toDp()
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
                    isEligible = uiState.isGeneralReviewEnabled,
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
        GroupVoteCreateContent(
            uiState = GroupVoteCreateUiState(
                pageText = "123",
                title = "가장 인상깊은 구절은?",
                options = listOf("1연 1행", "2연 3행", ""),
                bookTotalPage = 600,
                isGeneralReviewEnabled = true
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}