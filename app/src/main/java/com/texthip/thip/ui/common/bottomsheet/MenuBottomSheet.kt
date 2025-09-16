package com.texthip.thip.ui.common.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.group.room.mock.MenuBottomSheetItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun MenuBottomSheet(
    items: List<MenuBottomSheetItem>,
    onDismiss: () -> Unit
) {
    CustomBottomSheet(
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            items.forEachIndexed { index, item ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(modifier = Modifier.height(1.dp), color = colors.Grey03)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.text,
                        style = typography.menu_m500_s16_h24,
                        color = item.color,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.onClick()
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MenuBottomSheetPreview() {
    MenuBottomSheet(
        items = listOf(
            MenuBottomSheetItem(
                text = stringResource(R.string.leave_room),
                color = colors.White,
                onClick = { }
            ),
            MenuBottomSheetItem(
                text = stringResource(R.string.report_room),
                color = colors.Red,
                onClick = { }
            ),
            MenuBottomSheetItem(
                text = stringResource(R.string.delete_room),
                color = colors.Red,
                onClick = { }
            )
        ),
        onDismiss = {}
    )
}