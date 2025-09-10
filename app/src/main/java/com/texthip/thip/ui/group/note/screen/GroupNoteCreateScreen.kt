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
import com.texthip.thip.ui.group.note.component.OpinionInputSection
import com.texthip.thip.ui.group.note.component.PageInputSection
import com.texthip.thip.ui.group.note.viewmodel.GroupNoteCreateEvent
import com.texthip.thip.ui.group.note.viewmodel.GroupNoteCreateUiState
import com.texthip.thip.ui.group.note.viewmodel.GroupNoteCreateViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.utils.rooms.advancedImePadding

@Composable
fun GroupNoteCreateScreen(
    roomId: Int,
    recentPage: Int,
    totalPage: Int,
    isOverviewPossible: Boolean,
    postId: Int?,
    page: Int?,
    content: String?,
    isOverview: Boolean?,
    onBackClick: () -> Unit,
    onNavigateBackWithResult: () -> Unit,
    viewModel: GroupNoteCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.initialize(
            roomId, recentPage, totalPage, isOverviewPossible,
            postId, page, content, isOverview
        )
    }

    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBackWithResult()
        }
    }

    GroupNoteCreateContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun GroupNoteCreateContent(
    uiState: GroupNoteCreateUiState,
    onEvent: (GroupNoteCreateEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val density = LocalDensity.current
    var showTooltip by rememberSaveable { mutableStateOf(false) }

    // Tooltip 위치 측정용 state
    val iconCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .advancedImePadding()
    ) {
        Column {
            InputTopAppBar(
                title = if (uiState.isEditMode) {
                    stringResource(R.string.edit_record)
                } else {
                    stringResource(R.string.write_record)
                },
                isRightButtonEnabled = uiState.isFormFilled,
                onLeftClick = onBackClick,
                onRightClick = { onEvent(GroupNoteCreateEvent.CreateRecordClicked) }
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
                    onPageTextChange = { onEvent(GroupNoteCreateEvent.PageChanged(it)) },
                    isGeneralReview = uiState.isGeneralReview,
                    onGeneralReviewToggle = { onEvent(GroupNoteCreateEvent.GeneralReviewToggled(it)) },
                    isEligible = uiState.isOverviewPossible,
                    bookTotalPage = uiState.totalPage,
                    onInfoClick = { showTooltip = true },
                    onInfoPositionCaptured = { iconCoordinates.value = it },
                    isEnabled = !uiState.isEditMode
                )

                OpinionInputSection(
                    textFieldValue = uiState.opinionTextFieldValue,
                    onTextChange = { onEvent(GroupNoteCreateEvent.OpinionChanged(it)) }
                )
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
                    isEligible = uiState.isOverviewPossible,
                    onClose = { showTooltip = false }
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
    }
}

@Preview
@Composable
private fun GroupNoteCreateScreenPreview() {
    ThipTheme {
        GroupNoteCreateContent(
            uiState = GroupNoteCreateUiState(),
            onEvent = {},
            onBackClick = {}
        )
    }
}