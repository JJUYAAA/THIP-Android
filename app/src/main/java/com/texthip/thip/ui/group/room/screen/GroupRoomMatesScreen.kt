package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.data.model.rooms.response.RoomsUsersResponse
import com.texthip.thip.data.model.rooms.response.UserList
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.room.component.GroupRoomMatesList
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomMatesUiState
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomMatesViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomMatesScreen(
    roomId: Int,
    onBackClick: () -> Unit = {},
    onUserClick: (Int) -> Unit = {},
    viewModel: GroupRoomMatesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 화면이 처음 그려질 때 데이터 로딩 실행
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchUsers(roomId)
    }

    // UI 상태에 따라 다른 화면을 보여줌
    when (val state = uiState) {
        is GroupRoomMatesUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is GroupRoomMatesUiState.Success -> {
            // 성공 시, 실제 데이터를 화면에 표시
            GroupRoomMatesContent(
                data = state.users,
                onBackClick = onBackClick,
                onUserClick = onUserClick
            )
        }

        is GroupRoomMatesUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = colors.White) // TODO: 에러 메시지 스타일링
            }
        }
    }
}

@Composable
fun GroupRoomMatesContent(
    data: RoomsUsersResponse,
    onBackClick: () -> Unit = {},
    onUserClick: (Int) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.group_room_mates),
            onLeftClick = onBackClick,
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            GroupRoomMatesList(
                members = data,
                onUserClick = onUserClick,
            )
        }
    }
}

@Preview
@Composable
private fun GroupRoomMatesScreenPreview() {
    ThipTheme {
        GroupRoomMatesContent(
            data = RoomsUsersResponse(
                listOf(
                    UserList(
                        userId = 1,
                        nickname = "김희용",
                        alias = "문학가",
                        imageUrl = "https://example.com/image1.jpg",
                        followerCount = 100
                    ),
                    UserList(
                        userId = 2,
                        nickname = "노성준",
                        alias = "문학가",
                        imageUrl = "https://example.com/image1.jpg",
                        followerCount = 100
                    ),
                )
            ),
            onBackClick = {},
            onUserClick = {}
        )
    }
}