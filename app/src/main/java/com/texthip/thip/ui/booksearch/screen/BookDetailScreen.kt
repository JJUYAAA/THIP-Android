package com.texthip.thip.ui.booksearch.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.booksearch.mock.DetailBookData
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.modal.InfoPopup
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.common.topappbar.GradationTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    book: DetailBookData,
    feedList: List<String> = emptyList(),
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    onRecruitingGroupClick: () -> Unit = {},
    onBookMarkClick: (Boolean) -> Unit = {},
    onWriteFeedClick: () -> Unit = {}
) {
    var isAlarmVisible by remember { mutableStateOf(true) }
    var isIntroductionPopupVisible by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedFilterOption by remember { mutableIntStateOf(0) }

    val filterOptions = listOf(
        stringResource(R.string.search_filter_popular),
        stringResource(R.string.search_filter_latest)
    )

    // 알림 5초간 노출
    LaunchedEffect(Unit) {
        isAlarmVisible = true
        delay(5000)
        isAlarmVisible = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 메인 컨텐츠
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isIntroductionPopupVisible) {
                        Modifier.blur(4.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            if (book.coverImageRes != null) {
                Box(modifier = Modifier
                    .height(420.dp)
                    .fillMaxWidth()) {
                    Image(
                        painter = painterResource(book.coverImageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .blur(4.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        colors.Black.copy(alpha = 0.3f),
                                        colors.Black.copy(alpha = 0.6f),
                                        colors.Black.copy(alpha = 0.9f),
                                        colors.Black
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = isAlarmVisible) {
                    GradationTopAppBar(
                        isImageVisible = true,
                        count = book.participantsCount,
                        onLeftClick = {},
                        onRightClick = {}
                    )
                }
                AnimatedVisibility(visible = !isAlarmVisible) {
                    DefaultTopAppBar(
                        isRightIconVisible = true,
                        isTitleVisible = false,
                        onLeftClick = onLeftClick,
                        onRightClick = onRightClick
                    )
                }

                // 상세 정보 영역
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 40.dp),
                        text = book.title,
                        color = colors.White,
                        style = typography.bigtitle_b700_s22
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(
                            R.string.search_book_author,
                            book.author,
                            book.publisher
                        ),
                        color = colors.Grey,
                        style = typography.menu_sb600_s12_h20
                    )

                    Column(
                        modifier = Modifier
                            .padding(top = 33.dp)
                            .fillMaxWidth()
                            .clickable { isIntroductionPopupVisible = true }
                    ) {
                        Text(
                            text = stringResource(R.string.search_book_comment),
                            color = colors.White,
                            style = typography.menu_sb600_s14_h24,
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = book.description,
                            color = colors.Grey,
                            style = typography.copy_r400_s12_h20,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionMediumButton(
                            text = stringResource(
                                R.string.search_recruiting_group_count,
                                book.recruitingRoomCount
                            ),
                            contentColor = colors.Grey,
                            backgroundColor = Color.Transparent,
                            hasRightIcon = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = colors.Grey,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            onClick = onRecruitingGroupClick,
                        )
                        Row {
                            ActionMediumButton(
                                text = stringResource(R.string.search_write_feed_comment),
                                contentColor = colors.White,
                                backgroundColor = colors.Purple,
                                hasRightIcon = true,
                                hasRightPlusIcon = true,
                                modifier = Modifier.weight(1f),
                                onClick = onWriteFeedClick
                            )
                            Box(
                                modifier = modifier
                                    .padding(start = 12.dp)
                                    .size(44.dp)
                                    .border(
                                        width = 1.dp,
                                        color = colors.Grey02,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        isBookmarked = !isBookmarked
                                        onBookMarkClick(isBookmarked)
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (isBookmarked)
                                            R.drawable.ic_save_filled
                                        else
                                            R.drawable.ic_save
                                    ),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(44.dp))

                    Text(
                        text = stringResource(R.string.search_watch_feed),
                        color = colors.Grey,
                        style = typography.smalltitle_sb600_s18_h24,
                        modifier = Modifier.padding(bottom = 33.dp)
                    )

                    // 피드 리스트
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colors.DarkGrey02)
                    )
                    if (feedList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.search_no_feed_comment_1),
                                    color = colors.White,
                                    style = typography.smalltitle_sb600_s18_h24
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(R.string.search_no_feed_comment_2),
                                    color = colors.Grey01,
                                    style = typography.feedcopy_r400_s14_h20
                                )
                            }
                        }
                    } else {
                        // TODO: 피드 UI 구현 되면 수정
                    }
                }
            }

            FilterButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 462.dp, start = 20.dp, end = 20.dp),
                selectedOption = filterOptions[selectedFilterOption],
                options = filterOptions,
                onOptionSelected = { option ->
                    selectedFilterOption = filterOptions.indexOf(option)
                }
            )
        }

        if (isIntroductionPopupVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp)
            ) {
                InfoPopup(
                    title = stringResource(R.string.introduction),
                    content = book.description,
                    onDismiss = { isIntroductionPopupVisible = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBookDetailScreen() {
    ThipTheme {
        BookDetailScreen(
            book = DetailBookData(
                title = "채식주의자",
                author = "한강",
                publisher = "창비",
                description =
                    "인터내셔널 북커상, 산클레멘테 문학상 수상작. 전세계가 주목한 인간의 역작을 다시 만나다.2016년 인터내셔널 북커상을 수상하며 한국문학의 입지를 한단계 확장시킨 한강의 명단소설 『채식주의자』. 15년 만에 새로운 장정과 판형으로 출간된다. 식물화로 건설해온 극단적이며 실재적인 상상력의 강렬한 결실로 고통과 구속의 피안에 존재하는 인간의 본성에 다가간 작품." +
                            "인터내셔널 북커상, 산클레멘테 문학상 수상작. 전세계가 주목한 인간의 역작을 다시 만나다. \n\n2016년 인터내셔널 북커상을 수상하며 한국문학의 입지를 한단계 확장시킨 한강의 명단소설 『채식주의자』. 15년 만에 새로운 장정과 판형으로 출간된다. 식물화로 건설해온 극단적이며 실재적인 상상력의 강렬한 결실로 고통과 구속의 피안에 존재하는 인간의 본성에 다가간 작품.",
                coverImageRes = R.drawable.bookcover_sample,
                participantsCount = 210,
                recruitingRoomCount = 4
            ),
            feedList = emptyList()
        )
    }
}