package com.texthip.thip.ui.booksearch.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun BookEmptyResult(
    mainText: String,
    subText: String,
    onRequestBook: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            color = colors.White,
            style = typography.smalltitle_sb600_s18_h24
        )
        Text(
            text = subText,
            modifier = Modifier.padding(top = 8.dp),
            color = colors.Grey,
            style = typography.copy_r400_s14
        )
        Spacer(Modifier.height(20.dp))

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