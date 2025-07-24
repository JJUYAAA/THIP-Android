package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun EmptyBookSheetContent(
    onRequestBook: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.group_register_book_comment_1),
            color = colors.Grey,
            style = typography.copy_m500_s14_h20
        )

        Text(
            text = stringResource(R.string.group_register_book_comment_2),
            color = colors.Grey,
            style = typography.copy_m500_s14_h20
        )
        Spacer(Modifier.height(24.dp))

        ActionMediumButton(
            text = stringResource(R.string.group_register_book),
            contentColor = colors.White,
            backgroundColor = colors.Purple,
            modifier = Modifier
                .width(97.dp)
                .height(44.dp),
            onClick = { onRequestBook() },
        )
    }
}

@Preview
@Composable
private fun EmptyBookSheetContentPreview() {
    ThipTheme {
        EmptyBookSheetContent(
            onRequestBook = {}
        )
    }
}
