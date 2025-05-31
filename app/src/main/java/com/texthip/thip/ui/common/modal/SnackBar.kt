package com.texthip.thip.ui.common.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SnackBar(
    message: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(colors.DarkGrey02, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = colors.White,
                style = typography.view_m500_s12_h20
            )
            Text(
                text = actionText,
                color = colors.NeonGreen,
                style = typography.menu_sb600_s12_h20,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SnackBarPreview() {
    SnackBar(
        message = stringResource(R.string.complete_comment),
        actionText = stringResource(R.string.action_view_comment),
        onActionClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}