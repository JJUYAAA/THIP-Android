package com.texthip.thip.ui.signin.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.theme.Grey
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SigninDoneScreen(nickname: String, profileImageResId: Int?, role: String) {
    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            isRightIconVisible = false,
            isTitleVisible = false,
            onLeftClick = {},
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.hello_user, nickname),
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.signin_done),
                style = typography.copy_m500_s14_h20,
                color = colors.Grey,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(100.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (profileImageResId != null) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(width = 1.dp, color = colors.White, shape = CircleShape)
                            .background(colors.Black),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(id = profileImageResId),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(45.dp)
                                .offset(y = 2.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(width = 0.5.dp, color = colors.Grey01, shape = CircleShape)
                            .background(Grey)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nickname,
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = role,
                    style = typography.copy_r400_s14,
                    color = colors.White,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(76.dp))
                ActionMediumButton(
                    text = stringResource(R.string.start_thip),
                    contentColor = colors.White,
                    backgroundColor = colors.Purple,
                    modifier = Modifier.width(180.dp),
                    onClick = {},
                )
            }

        }

    }
}

@Preview
@Composable
private fun SigninDoneScreenPrev() {
    SigninDoneScreen("JJUYAA", profileImageResId = R.drawable.character_sociology, role = "칭호칭호")
}