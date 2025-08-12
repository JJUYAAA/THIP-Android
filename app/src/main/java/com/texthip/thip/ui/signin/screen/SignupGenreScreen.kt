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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.mypage.component.RoleCard
import com.texthip.thip.ui.mypage.mock.RoleItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun SignupGenreScreen() {
    var selectedIndex by rememberSaveable { mutableStateOf(-1) }
    val isRightButtonEnabled by remember { derivedStateOf { selectedIndex != -1 } }

    val roleCards = listOf(
        RoleItem(
            stringResource(R.string.literature),
            stringResource(R.string.literary_person),
            R.drawable.character_literature,
            colors.Literature
        ),
        RoleItem(
            stringResource(R.string.science_it),
            stringResource(R.string.scientist),
            R.drawable.character_science,
            colors.ScienceIt
        ),
        RoleItem(
            stringResource(R.string.social_science),
            stringResource(R.string.sociologist),
            R.drawable.character_sociology,
            colors.SocialScience
        ),
        RoleItem(
            stringResource(R.string.art),
            stringResource(R.string.artist),
            R.drawable.character_art,
            colors.Art
        ),
        RoleItem(
            stringResource(R.string.humanities),
            stringResource(R.string.philosopher),
            R.drawable.character_humanities,
            colors.Humanities
        )
    )

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
            onRightClick = {
                // TODO 다음 화면으로 이동
            }
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
                items(roleCards.size) { index ->
                    RoleCard(
                        genre = roleCards[index].genre,
                        role = roleCards[index].role,
                        imageResId = roleCards[index].imageResId,
                        genreColor = colors.White,
                        roleColor = roleCards[index].roleColor,
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun SignupGenreScreenPrev() {
    ThipTheme {
        SignupGenreScreen()
    }
}