package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.mypage.component.BookContent
import com.texthip.thip.ui.mypage.component.FeedContent
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun SavedScreen() {
    val tabs = listOf(stringResource(R.string.feed), stringResource(R.string.book))
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.saved),
            onLeftClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.width(160.dp).padding(start = 20.dp)) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = colors.White,
                    indicator = { tabPositions ->
                        val tabPosition = tabPositions[selectedTabIndex]

                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPosition)
                                .width(48.dp)
                                .height(2.dp)
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(White)
                        )
                    },
                    divider = {}) {

                    tabs.forEachIndexed { index, title ->
                        val selected = selectedTabIndex == index
                        Tab(
                            modifier = Modifier.width(60.dp),
                            selected = selected,
                            onClick = { selectedTabIndex = index },
                            selectedContentColor = colors.White,
                            unselectedContentColor = colors.Grey02,
                            text = {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = title,
                                    style = typography.smalltitle_sb600_s18_h24
                                )
                            }
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                when (selectedTabIndex) {
                    0 -> FeedContent()
                    1 -> BookContent()
                }
            }
        }
    }
}


@Preview
@Composable
private fun SavedScreenPrev() {
    SavedScreen()
}