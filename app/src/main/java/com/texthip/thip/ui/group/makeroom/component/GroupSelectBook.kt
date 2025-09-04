package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.texthip.thip.ui.common.cards.CardInputBook
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupSelectBook(
    selectedBook: BookData?,
    onChangeBookClick: () -> Unit,
    onSelectBookClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBookPreselected: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.group_select_book_title),
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (selectedBook == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable { onSelectBookClick() }
                    .fillMaxWidth()
                    .height(44.dp)
                    .border(
                        BorderStroke(1.dp, colors.Grey02),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "검색 아이콘",
                    tint = colors.Grey01
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.group_book_search),
                    style = typography.menu_m500_s16_h24,
                    color = colors.Grey
                )
            }
        } else {
            CardInputBook(
                title = selectedBook.title,
                author = selectedBook.author ?: "",
                imageUrl = selectedBook.imageUrl,
                imageRes = R.drawable.img_book_cover_sample,
                showChangeButton = !isBookPreselected, // 사전 선택된 책인 경우 변경 버튼 숨김
                onChangeClick = onChangeBookClick
            )
        }
    }
}


private val dummyBook = BookData(
    title = "호르몬 체인지",
    imageUrl = null,
    author = "최정화"
)

@Preview(showBackground = true)
@Composable
fun GroupSelectBookPreview_Unselected() {
    ThipTheme {
        GroupSelectBook(
            selectedBook = null,
            onChangeBookClick = {},
            onSelectBookClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupSelectBookPreview_Selected() {
    ThipTheme {
        GroupSelectBook(
            selectedBook = dummyBook,
            onChangeBookClick = {},
            onSelectBookClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupSelectBookPreview_Preselected() {
    ThipTheme {
        GroupSelectBook(
            selectedBook = dummyBook,
            onChangeBookClick = {},
            onSelectBookClick = {},
            isBookPreselected = true
        )
    }
}
