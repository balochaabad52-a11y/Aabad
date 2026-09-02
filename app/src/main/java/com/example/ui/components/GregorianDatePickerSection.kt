package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GregorianDate
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Gold900
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.utils.HijriCalendarCalculator

@Composable
fun GregorianDatePickerSection(
    selectedDate: GregorianDate,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    moonOffset: Int,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = true
) {
    var viewYear by remember(selectedDate.year) { mutableIntStateOf(selectedDate.year) }
    var viewMonth by remember(selectedDate.month) { mutableIntStateOf(selectedDate.month) }

    val today = remember { HijriCalendarCalculator.getTodayGregorian() }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = Color.White.copy(alpha = 0.95f),
        border = BorderStroke(1.dp, Slate200),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Month / Year and Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isUrdu) {
                            "${GregorianDate.GREGORIAN_MONTHS_UR.getOrElse(viewMonth - 1) { "" }} ${viewYear}ء"
                        } else {
                            "${GregorianDate.GREGORIAN_MONTHS_EN.getOrElse(viewMonth - 1) { "" }} $viewYear"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = if (isUrdu) "شمسی تقویم (عیسوی کیلنڈر)" else "Gregorian Calendar (شمسی تقویم)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200)
                    ) {
                        IconButton(
                            onClick = {
                                if (viewMonth == 1) {
                                    viewMonth = 12
                                    viewYear -= 1
                                } else {
                                    viewMonth -= 1
                                }
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("greg_prev_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (isUrdu) "پچھلا مہینہ" else "Previous Month",
                                tint = Slate700,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Jump to Today
                    Surface(
                        shape = CircleShape,
                        color = Emerald50,
                        border = BorderStroke(1.dp, Emerald600.copy(alpha = 0.3f))
                    ) {
                        IconButton(
                            onClick = {
                                viewYear = today.year
                                viewMonth = today.month
                                onDateSelected(today.year, today.month, today.day)
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("greg_today_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Today,
                                contentDescription = if (isUrdu) "آج کا دن" else "Jump to Today",
                                tint = Emerald700,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200)
                    ) {
                        IconButton(
                            onClick = {
                                if (viewMonth == 12) {
                                    viewMonth = 1
                                    viewYear += 1
                                } else {
                                    viewMonth += 1
                                }
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("greg_next_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = if (isUrdu) "اگلا مہینہ" else "Next Month",
                                tint = Slate700,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekday Headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val daysLabels = if (isUrdu) {
                    listOf("اتوار", "پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ")
                } else {
                    listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                }
                daysLabels.forEachIndexed { index, dayName ->
                    val isFriday = index == 5
                    Text(
                        text = dayName,
                        modifier = Modifier.width(42.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = if (isUrdu) 11.sp else 12.sp),
                        fontWeight = if (isFriday) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isFriday) Emerald700 else Slate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days Grid
            val daysInMonth = HijriCalendarCalculator.getDaysInGregorianMonth(viewYear, viewMonth)
            val firstDayOfWeek = run {
                val jd = HijriCalendarCalculator.gregorianToJulianDay(viewYear, viewMonth, 1)
                (((jd + 1) % 7 + 7) % 7 + 1).toInt() // 1 = Sun, 2 = Mon ... 7 = Sat
            }
            val leadingEmptyDays = firstDayOfWeek - 1

            val totalSlots = leadingEmptyDays + daysInMonth
            val rows = (totalSlots + 6) / 7

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (rowIndex in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (colIndex in 0 until 7) {
                            val slotIndex = rowIndex * 7 + colIndex
                            val dayNum = slotIndex - leadingEmptyDays + 1

                            if (dayNum in 1..daysInMonth) {
                                val isSelected = viewYear == selectedDate.year &&
                                        viewMonth == selectedDate.month &&
                                        dayNum == selectedDate.day
                                val isToday = viewYear == today.year &&
                                        viewMonth == today.month &&
                                        dayNum == today.day
                                val isFriday = colIndex == 5

                                // Calculate corresponding Hijri day for mini display
                                val hijriForDay = HijriCalendarCalculator.toHijri(viewYear, viewMonth, dayNum, moonOffset)

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when {
                                                isSelected -> Emerald600
                                                isToday -> Emerald50
                                                else -> Color.Transparent
                                            }
                                        )
                                        .then(
                                            when {
                                                isToday && !isSelected -> Modifier.border(1.5.dp, Emerald600, RoundedCornerShape(12.dp))
                                                isFriday && !isSelected -> Modifier.border(1.dp, Gold400.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                                else -> Modifier
                                            }
                                        )
                                        .clickable {
                                            onDateSelected(viewYear, viewMonth, dayNum)
                                        }
                                        .testTag("greg_day_$dayNum"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "$dayNum",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                                            color = when {
                                                isSelected -> Color.White
                                                isToday -> Emerald700
                                                isFriday -> Emerald700
                                                else -> Slate800
                                            }
                                        )
                                        // Mini Hijri Day Number underneath
                                        Text(
                                            text = "${hijriForDay.day}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            color = when {
                                                isSelected -> Gold300
                                                isFriday -> Gold900
                                                else -> Slate400
                                            }
                                        )
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.size(42.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar Legend / Helper row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Emerald600)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isUrdu) "منتخب شدہ" else "Selected",
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Gold500)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isUrdu) "جمعۃ المبارک" else "Jumu'ah",
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate600
                    )
                }

                Text(
                    text = if (isUrdu) "نیچے: ہجری تاریخ" else "Sub: Hijri Day",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate400
                )
            }
        }
    }
}

private val Gold300 = Color(0xFFFDE68A)
