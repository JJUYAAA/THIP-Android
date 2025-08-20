package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun GroupRoomDurationPicker(
    modifier: Modifier = Modifier,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit = { _, _ -> }
) {
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val maxDate = today.plusMonths(12)
    var isInitialized by rememberSaveable { mutableStateOf(false) }

    var startDate by rememberSaveable { mutableStateOf(tomorrow) }
    var endDate by rememberSaveable { mutableStateOf(tomorrow.plusDays(1)) }
    var isPickerTouched by rememberSaveable { mutableStateOf(false) }

    // 첫 시작 시에만 모든 날짜를 내일 기준으로 초기화
    LaunchedEffect(Unit) {
        if (!isInitialized) {
            startDate = tomorrow
            endDate = tomorrow.plusDays(1)
            isInitialized = true
        }
    }

    // 날짜 범위 계산
    val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
    val isOverLimit = daysBetween > 91

    // 날짜 선택 콜백
    LaunchedEffect(startDate, endDate) {
        if (endDate.isAfter(startDate)) {
            onDateRangeSelected(startDate, endDate)
        }
    }

    // 날짜 유효성 검사 및 자동 조정
    LaunchedEffect(startDate) {
        val adjustedStartDate = when {
            startDate.isBefore(tomorrow) -> tomorrow
            startDate.isAfter(maxDate) -> maxDate
            else -> startDate
        }

        if (adjustedStartDate != startDate) {
            startDate = adjustedStartDate
        }

        // 끝 날짜가 시작 날짜보다 빠르면 조정
        if (endDate.isBefore(startDate.plusDays(1))) {
            endDate = startDate.plusDays(1)
        }
    }

    LaunchedEffect(endDate) {
        val adjustedEndDate = when {
            endDate.isAfter(maxDate) -> maxDate
            endDate.isBefore(startDate.plusDays(1)) -> startDate.plusDays(1)
            else -> endDate
        }

        if (adjustedEndDate != endDate) {
            endDate = adjustedEndDate
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.group_room_duration_title),
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 시작 날짜 Picker
            GroupDatePicker(
                selectedDate = startDate,
                minDate = tomorrow,
                maxDate = maxDate,
                onDateSelected = { newDate ->
                    startDate = newDate
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { isPickerTouched = true }
                        )
                    }
            )

            // 구분자
            Text(
                text = "~",
                style = typography.info_r400_s12,
                color = colors.White,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // 끝 날짜 Picker
            GroupDatePicker(
                selectedDate = endDate,
                minDate = tomorrow,
                maxDate = maxDate,
                onDateSelected = { newDate ->
                    endDate = newDate
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { isPickerTouched = true }
                        )
                    }
            )
        }

        // 안내/에러 메시지
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            when {
                isOverLimit -> {
                    Text(
                        text = stringResource(R.string.group_room_duration_comment),
                        style = typography.info_r400_s12,
                        color = colors.Red,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                !isPickerTouched -> {
                    Text(
                        text = stringResource(R.string.group_room_duration_initial_comment),
                        style = typography.info_r400_s12,
                        color = colors.NeonGreen,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                else -> {
                    Text(
                        text = stringResource(
                            R.string.group_room_duration_active_comment,
                            startDate.monthValue,
                            startDate.dayOfMonth
                        ),
                        style = typography.info_r400_s12,
                        color = colors.NeonGreen,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingDurationPickerPreview() {
    ThipTheme {
        GroupRoomDurationPicker { startDate, endDate ->
        }
    }
}