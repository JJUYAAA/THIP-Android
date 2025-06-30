package com.texthip.thip.ui.myPage.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.texthip.thip.ui.common.forms.BaseInputTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.myPage.component.RoleCard
import com.texthip.thip.ui.myPage.mock.RoleItem
import com.texthip.thip.ui.theme.Black
import com.texthip.thip.ui.theme.Lavendar
import com.texthip.thip.ui.theme.NeonGreen
import com.texthip.thip.ui.theme.Orange
import com.texthip.thip.ui.theme.Pink
import com.texthip.thip.ui.theme.Skyblue
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun EditProfileScreen() {
    var selectedIndex by rememberSaveable { mutableStateOf(-1) }
    val roleCards = listOf(
        RoleItem(
            stringResource(R.string.literature),
            stringResource(R.string.literary_person),
            R.drawable.character_literature,
            NeonGreen
        ),
        RoleItem(
            stringResource(R.string.science_it),
            stringResource(R.string.scientist),
            R.drawable.character_science,
            Lavendar
        ),
        RoleItem(
            stringResource(R.string.social_science),
            stringResource(R.string.sociologist),
            R.drawable.character_sociology,
            Orange
        ),
        RoleItem(
            stringResource(R.string.art),
            stringResource(R.string.artist),
            R.drawable.character_art,
            Pink
        ),
        RoleItem(
            stringResource(R.string.humanities),
            stringResource(R.string.philosopher),
            R.drawable.character_humanities,
            Skyblue
        )
    )
    Scaffold(
        containerColor = Black,
        topBar = {
            InputTopAppBar(
                title = stringResource(R.string.edit_profile),
                isRightButtonEnabled = true,
                onLeftClick = {},
                onRightClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.change_nickname),
                style = typography.smalltitle_sb600_s18_h24,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            //TODO 컴포넌트 수정 필요 -> text count 추가, boolean 값으로 icon, limit 설정가능하도록
            BaseInputTextField(
                hint = stringResource(R.string.change_nickname)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.edit_role),
                style = typography.smalltitle_sb600_s18_h24,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.role_description),
                style = typography.copy_r400_s14,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Text(
                text = stringResource(R.string.choice_one),
                style = typography.info_r400_s12,
                color = NeonGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )


            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                roleCards.forEachIndexed { index, RoleItem ->
                    RoleCard(
                        genre = RoleItem.genre,
                        role = RoleItem.role,
                        imageResId = RoleItem.imageResId,
                        genreColor = White,
                        roleColor = RoleItem.roleColor,
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
private fun EditProfileScreenPrev() {
    EditProfileScreen()
}