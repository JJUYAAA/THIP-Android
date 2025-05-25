package com.texthip.thip.ui.common.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R

@Composable
fun DialogPopup(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(320.dp)
            .height(182.dp)
            .background(
                color = colorResource(id = R.color.darkgray),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.primary_white),
                    fontFamily = FontFamily(Font(R.font.medium))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.primary_white),
                    fontFamily = FontFamily(Font(R.font.regular))
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.secondary_gray02)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "아니오",
                        color = colorResource(id = R.color.primary_white),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.semibold))
                    )
                }

                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.button_primary)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "예",
                        color = colorResource(id = R.color.primary_white),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.semibold))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogPopup() {
    DialogPopup(
        title = "대화창의 폐쇄형 질문입니다?",
        description = "대화창의 디스크립션을 입력합니다. 대화창의 디스크립션을 입력합니다.",
        onConfirm = {},
        onCancel = {}
    )
}