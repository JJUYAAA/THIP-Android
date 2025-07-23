<<<<<<<< HEAD:app/src/main/java/com/texthip/thip/ui/group/search/component/GroupRecentSearch.kt
package com.texthip.thip.ui.group.search.component
========
package com.texthip.thip.ui.group.myroom.component
>>>>>>>> 1d57638dfea70853ea5744e306675cd321e496c4:app/src/main/java/com/texthip/thip/ui/group/myroom/component/GroupRecentSearch.kt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.theme.ThipTheme
<<<<<<<< HEAD:app/src/main/java/com/texthip/thip/ui/group/search/component/GroupRecentSearch.kt
========
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
>>>>>>>> 1d57638dfea70853ea5744e306675cd321e496c4:app/src/main/java/com/texthip/thip/ui/group/myroom/component/GroupRecentSearch.kt

@Composable
fun GroupRecentSearch(
    recentSearches: List<String>,
    onSearchClick: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.group_recent_search),
        color = colors.White,
        style = typography.menu_r400_s14_h24
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (recentSearches.isEmpty()) {
        Text(
            text = stringResource(R.string.group_no_recent_search),
            color = colors.Grey01,
            style = typography.menu_r400_s14_h24
        )
    } else {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            maxLines = 2,
        ) {
            recentSearches.take(9).forEach { keyword ->
                GenreChipButton(
                    text = keyword,
                    onClick = { onSearchClick(keyword) },
                    onCloseClick = { onRemove(keyword) }
                )
            }
        }
    }
}
