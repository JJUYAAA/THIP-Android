package com.texthip.thip.ui.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun HeaderMenuBarTab(
    modifier: Modifier = Modifier,
    titles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    indicatorColor: Color = colors.White,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = colors.White,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            val tabPosition = tabPositions[selectedTabIndex]
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPosition)
                    .padding(horizontal = 20.dp)
                    .height(2.dp)
                    .background(indicatorColor, shape = RoundedCornerShape(1.5.dp))
            )
        },
        divider = {},
        modifier = modifier
    ) {
        titles.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            Tab(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .height(40.dp),
                selected = selected,
                onClick = { onTabSelected(index) },
                selectedContentColor = colors.White,
                unselectedContentColor = colors.Grey02,
                text = {
                    Text(
                        text = title,
                        style = typography.smalltitle_sb600_s18_h24,
                        textAlign = TextAlign.Start
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun HeaderMenuBarTabPreview() {
    var selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    Box(modifier = Modifier) {
        HeaderMenuBarTab(
            titles = listOf("그룹 기록", "내 기록"),
            selectedTabIndex = selectedIndex.value,
            onTabSelected = { selectedIndex.value = it }
        )
    }
}