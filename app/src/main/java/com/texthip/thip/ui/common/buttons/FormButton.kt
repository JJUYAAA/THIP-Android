package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.PageTextField
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FormButton(
    modifier: Modifier = Modifier,
    firstPage: String,
    onFirstPageChange: (String) -> Unit,
    lastPage: String,
    onLastPageChange: (String) -> Unit,
    onFinishClick: () -> Unit
) {
    val isSendEnabled = firstPage.isNotBlank() || lastPage.isNotBlank()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(color = colors.DarkGrey, shape = RoundedCornerShape(20.dp))
            .padding(start = 12.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageTextField(
                text = firstPage,
                onTextChange = onFirstPageChange
            )

            Text(
                text = stringResource(R.string.tilde),
                style = typography.copy_r400_s14,
                color = colors.White
            )

            PageTextField(
                text = lastPage,
                onTextChange = onLastPageChange
            )

            Text(
                text = stringResource(R.string.page),
                style = typography.copy_r400_s14,
                color = colors.White
            )

            SendButton(
                icon = R.drawable.ic_reset,
                enabled = isSendEnabled
            ) {
                onFirstPageChange("")
                onLastPageChange("")
            }
        }

        SendButton(
            icon = R.drawable.ic_check,
            onClick = onFinishClick
        )
    }
}

@Preview
@Composable
private fun FormButtonPreview() {
    var firstPage by rememberSaveable { mutableStateOf("") }
    var lastPage by rememberSaveable { mutableStateOf("") }

    FormButton(
        firstPage = firstPage,
        onFirstPageChange = { firstPage = it },
        lastPage = lastPage,
        onLastPageChange = { lastPage = it },
        onFinishClick = {}
    )
}