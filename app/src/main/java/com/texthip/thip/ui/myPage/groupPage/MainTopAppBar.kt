package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun MainTopAppBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 로고 (임시)

        Text("THIP!", color = colors.NeonGreen)

        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_done),
            contentDescription = "다 한거",
            tint = colors.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_notice),
            contentDescription = "알림",
            tint = colors.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360)
@Composable
fun PreviewMainTopAppBar() {
    MainTopAppBar()
}

