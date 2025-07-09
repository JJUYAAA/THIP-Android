package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun SectionDivider() {
    Spacer(modifier = Modifier.padding(top = 32.dp))
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.DarkGrey02)
    )
    Spacer(modifier = Modifier.padding(top = 32.dp))
}