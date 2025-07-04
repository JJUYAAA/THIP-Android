package com.texthip.thip.ui.group.makeroom.component

import android.R.attr.contentDescription
import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupSelectBook(modifier: Modifier = Modifier, onButtonClick: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "책 선택 화면",
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .clickable { onButtonClick() }
                .fillMaxWidth()
                .height(44.dp)
                .border(
                    BorderStroke(1.dp, colors.Grey02),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "검색 아이콘",
                tint = colors.Grey01
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "검색해서 찾기",
                style = typography.menu_m500_s16_h24,
                color = colors.Grey
            )
        }
    }
}

@Preview
@Composable
private fun GroupSelectBookPreview() {
    ThipTheme {
        GroupSelectBook()
    }
}