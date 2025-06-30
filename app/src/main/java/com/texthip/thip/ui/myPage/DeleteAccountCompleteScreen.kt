package com.texthip.thip.ui.myPage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.theme.Black
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White


@Composable
fun DeleteAccountCompleteScreen() {
    Scaffold(
        containerColor = Black,
        topBar = {
            DefaultTopAppBar(
                isRightIconVisible = false,
                isTitleVisible = false,
                onLeftClick = {},
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.delete_account_complete),
                style = typography.title_b700_s20_h24,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Text(
                text = stringResource(R.string.see_you_again),
                style = typography.menu_m500_s16_h24,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.character_humanities),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )
                Image(
                    painter = painterResource(R.drawable.character_art),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )
                Image(
                    painter = painterResource(R.drawable.character_science),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )
                Image(
                    painter = painterResource(R.drawable.character_literature),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAccountCompleteScreenPrev() {
    DeleteAccountCompleteScreen()
}