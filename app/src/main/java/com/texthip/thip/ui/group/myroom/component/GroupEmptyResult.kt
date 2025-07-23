<<<<<<<< HEAD:app/src/main/java/com/texthip/thip/ui/group/search/component/GroupEmptyResult.kt
package com.texthip.thip.ui.group.search.component
========
package com.texthip.thip.ui.group.myroom.component
>>>>>>>> 1d57638dfea70853ea5744e306675cd321e496c4:app/src/main/java/com/texthip/thip/ui/group/myroom/component/GroupEmptyResult.kt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
<<<<<<<< HEAD:app/src/main/java/com/texthip/thip/ui/group/search/component/GroupEmptyResult.kt
========
import com.texthip.thip.ui.theme.ThipTheme
>>>>>>>> 1d57638dfea70853ea5744e306675cd321e496c4:app/src/main/java/com/texthip/thip/ui/group/myroom/component/GroupEmptyResult.kt
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupEmptyResult(
    mainText: String,
    subText: String
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
    }
}
