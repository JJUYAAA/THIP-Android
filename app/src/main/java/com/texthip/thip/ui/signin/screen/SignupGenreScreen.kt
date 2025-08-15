package com.texthip.thip.ui.signin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.mypage.component.RoleCard
import com.texthip.thip.ui.mypage.mock.RoleItem
import com.texthip.thip.ui.signin.viewmodel.SignupAliasUiState
import com.texthip.thip.ui.signin.viewmodel.SignupAliasViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SignupGenreScreen(
    onNavigateToNext: (RoleItem) -> Unit, // 선택된 아이템 정보를 다음 화면으로 넘겨주기
    viewModel: SignupAliasViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignupGenreContent(
        uiState = uiState,
        onCardSelected = { index -> viewModel.selectCard(index) },
        onNextClick = {
            // 선택된 아이템이 있을 경우에만 다음 화면으로 이동
            uiState.roleCards.getOrNull(uiState.selectedIndex)?.let { selectedRoleItem ->
                onNavigateToNext(selectedRoleItem)
            }
        }
    )
}
@Composable
fun SignupGenreContent(
    uiState: SignupAliasUiState,
    onCardSelected: (Int) -> Unit,
    onNextClick: () -> Unit
) {
    val isRightButtonEnabled = uiState.selectedIndex != -1 && !uiState.isLoading

    Column(
        Modifier
            .fillMaxSize()
    ) {
        InputTopAppBar(
            title = stringResource(R.string.settings_2),
            isRightButtonEnabled = isRightButtonEnabled,
            rightButtonName = stringResource(R.string.next),
            isLeftIconVisible = false,
            onLeftClick = {},
            onRightClick = onNextClick
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.select_genre),
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.genre_can_be_changed),
                style = typography.copy_r400_s14,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 152.dp), // 카드 최소 크기
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
            ) {
                itemsIndexed(uiState.roleCards) { index, roleItem ->
                    RoleCard(
                        genre = roleItem.genre,
                        role = roleItem.role,
                        imageUrl = roleItem.imageUrl,
                        roleColor = roleItem.roleColor,
                        selected = uiState.selectedIndex == index,
                        onClick = { onCardSelected(index) }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun SignupGenreScreenPrev() {
    val previewRoleCards = listOf(
        RoleItem("문학", "문학가", "", "#FFFFFF"),
        RoleItem("과학/IT", "과학자", "", "#FFFFFF"),
        RoleItem("사회", "사회학자", "", "#FFFFFF"),
        RoleItem("예술", "예술가", "", "#FFFFFF"),
        RoleItem("인문", "철학자", "", "#FFFFFF")
    )
    val previewUiState = SignupAliasUiState(
        roleCards = previewRoleCards,
        selectedIndex = 1 // 1번 아이템이 선택된 상태로 프리뷰
    )

    ThipTheme {
        SignupGenreContent(
            uiState = previewUiState,
            onCardSelected = {},
            onNextClick = {}
        )
    }
}