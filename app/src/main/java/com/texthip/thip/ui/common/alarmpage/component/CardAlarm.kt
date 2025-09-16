package com.texthip.thip.ui.common.alarmpage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun CardAlarm(
    modifier: Modifier = Modifier,
    badgeText: String,
    title: String,
    message: String,
    timeAgo: String,
    isRead: Boolean = false,
    containerColorUnread: Color = colors.DarkGrey02,  // 안읽음 상태의 배경색
    containerColorRead: Color = colors.DarkGrey03, // 읽음 상태의 배경색
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) containerColorRead else containerColorUnread
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 뱃지
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(13.dp))
                        .border(
                            width = 1.dp,
                            color = if (isRead) colors.Grey02 else colors.Grey01,
                            shape = RoundedCornerShape(13.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        text = badgeText,
                        color = if (isRead) colors.Grey01 else colors.Grey,
                        style = typography.menu_sb600_s12_h20
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 내용 (제목, 빨간 점, 시간, 메시지)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = typography.view_m500_s14,
                            color = if (isRead) colors.Grey01 else colors.White,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            // 빨간 점을 위한 고정 공간 (항상 6dp 높이 유지)
                            Box(
                                modifier = Modifier
                                    .size(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // 안읽음 상태일 때만 빨간 점 표시
                                if (!isRead) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(color = colors.Red)
                                    )
                                }
                            }

                            Text(
                                text = timeAgo,
                                style = typography.timedate_r400_s11,
                                color = if (isRead) colors.Grey02 else colors.Grey01,
                                modifier = Modifier
                                    .padding(top = 7.dp, end = 2.dp)
                                    .offset(y = (-5).dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = message,
                style = typography.copy_r400_s12_h20,
                color = if (isRead) colors.Grey02 else colors.Grey01,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun PreviewNotificationCards() {
    var isRead by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 안읽은 알림
        CardAlarm(
            title = "같이 읽기를 시작했어요!",
            badgeText = "모임",
            message = "한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다.",
            timeAgo = "12시간 전",
            isRead = isRead
        ) {
            isRead = true
        }

        // 읽은 알림
        CardAlarm(
            title = "같이 읽기를 시작했어요!",
            badgeText = "모임",
            message = "한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다.",
            timeAgo = "12시간 전",
            isRead = true
        )

        CardAlarm(
            title = "같이 읽기를 시작했어요!",
            badgeText = "피드",
            message = "한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다.",
            timeAgo = "12시간 전",
            isRead = false
        )

        CardAlarm(
            title = "같이 읽기를 시작했어요!",
            badgeText = "좋아요",
            message = "한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다.",
            timeAgo = "12시간 전",
            isRead = isRead
        )

        CardAlarm(
            title = "같이 읽기를 시작했어요!",
            badgeText = "댓글",
            message = "한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다. 한줄만 입력이 가능합니다.",
            timeAgo = "12시간 전",
            isRead = isRead
        )
    }

}