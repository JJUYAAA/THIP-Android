package com.texthip.thip.ui.group.room.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor

@Composable
fun GroupRoomHeader(
    roomName: String,
    roomDescription: String,
    isPublic: Boolean,
    progressStartDate: String,
    progressEndDate: String,
    memberCount: Int,
    category: String,
    categoryColor: String = "#A0F8E8",
    onNavigateToMates: () -> Unit = { }
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = roomName,
                style = typography.bigtitle_b700_s22_h24,
                color = colors.White
            )

            if (!isPublic) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = "Lock Icon",
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Column {
            Text(
                text = stringResource(R.string.introduction_content),
                style = typography.menu_sb600_s14_h24,
                color = colors.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = roomDescription,
                style = typography.copy_r400_s12_h20,
                color = colors.White
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 180.dp)
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar),
                        contentDescription = "Calendar Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.group_active_period),
                        style = typography.view_m500_s12_h20,
                        color = colors.White
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(
                        R.string.group_room_period,
                        progressStartDate,
                        progressEndDate
                    ),
                    style = typography.timedate_r400_s11,
                    color = colors.White
                )
            }

            Column(
                modifier = Modifier
                    .widthIn(max = 180.dp)
                    .clickable { onNavigateToMates() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_group),
                            contentDescription = "Group Icon",
                            tint = colors.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = stringResource(R.string.book_mate),
                            style = typography.view_m500_s12_h20,
                            color = colors.White
                        )
                    }

                    IconButton(
                        onClick = { /* TODO: Navigate to participant list */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron),
                            contentDescription = "Participant Icon",
                            tint = colors.White
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.participate_num, memberCount),
                    style = typography.menu_sb600_s12,
                    color = colors.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .background(color = colors.Grey03, shape = RoundedCornerShape(40.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.genre),
                    style = typography.info_m500_s12,
                    color = colors.White,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = category,
                    style = typography.info_m500_s12,
                    color = hexToColor(categoryColor)
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupRoomHeaderPreview() {
    GroupRoomHeader(
        roomName = "호르몬 체인지 완독하는 방",
        roomDescription = "‘시집만 읽는 사람들’ 3월 모임입니다. 이번 달 모임방은 심장보다 단단한 토마토 한 알 완독합니다.",
        isPublic = true,
        progressStartDate = "2023.10.01",
        progressEndDate = "2023.10.31",
        memberCount = 22,
        category = "문학"
    )
}