package com.texthip.thip.ui.common.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

data class FabMenuItem(
    val icon: Painter,
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ExpandableFloatingButton(
    menuItems: List<FabMenuItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 32.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            // 펼쳐진 메뉴
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 94.dp, end = 20.dp)
                        .width(184.dp)
                        .background(
                            color = colors.Black,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = colors.NeonGreen,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 24.dp, horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    menuItems.forEachIndexed { index, item ->
                        MenuItem(
                            icon = item.icon,
                            text = item.text,
                            onClick = {
                                expanded = false
                                item.onClick()
                            }
                        )
                    }
                }
            }
        }

        // 플로팅 버튼
        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = if (expanded) colors.Purple else colors.DarkGrey,
            contentColor = colors.NeonGreen,
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
                .border(width = 2.dp, color = colors.NeonGreen50, shape = CircleShape)
        ) {
            Icon(
                painter = if (expanded) painterResource(R.drawable.ic_x) else painterResource(R.drawable.ic_plus),
                modifier = Modifier.size(24.dp),
                tint = colors.NeonGreen,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MenuItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = colors.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = colors.White,
            style = typography.menu_m500_s16_h24
        )
    }
}

@Preview
@Composable
private fun ExpandableFabPreview() {
    ExpandableFloatingButton(
        menuItems = listOf(
            FabMenuItem(
                icon = painterResource(R.drawable.ic_write),
                text = stringResource(R.string.write_record),
                onClick = { }
            ),
            FabMenuItem(
                icon = painterResource(R.drawable.ic_vote),
                text = stringResource(R.string.create_vote),
                onClick = { }
            )
        )
    )
}