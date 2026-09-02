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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HijriDate
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Gold700
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
fun HijriDatePickerSection(
    selectedHijri: HijriDate,
    onHijriDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    moonOffset: Int,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = true
) {
    var viewYear by remember(selectedHijri.year) { mutableIntStateOf(selectedHijri.year) }
    var viewMonth by remember(selectedHijri.month) { mutableIntStateOf(selectedHijri.month) }

    val todayHijri = remember(moonOffset) { HijriCalendarCalculator.getTodayHijri(moonOffset) }

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
            // Header: Hijri Month & Year Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isUrdu) {
                                "${HijriDate.HIJRI_MONTHS_UR.getOrElse(viewMonth - 1) { "" }} $viewYear ھ"
                            } else {
                                "${HijriDate.HIJRI_MONTHS_EN.getOrElse(viewMonth - 1) { "" }} $viewYear AH"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.NightsStay,
                            contentDescription = if (isUrdu) "قمری اسلامی تقویم" else "Hijri Lunar",
                            tint = Emerald600,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Arabic & Subtitle
                    Text(
                        text = if (isUrdu) {
                            "${HijriDate.HIJRI_MONTHS_AR.getOrElse(viewMonth - 1) { "" }}  •  اسلامی قمری تقویم"
                        } else {
                            "${HijriDate.HIJRI_MONTHS_AR.getOrElse(viewMonth - 1) { "" }}  •  ${HijriDate.HIJRI_MONTHS_UR.getOrElse(viewMonth - 1) { "" }}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Emerald700
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
                                .testTag("hijri_prev_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (isUrdu) "پچھلا مہینہ" else "Previous Islamic Month",
                                tint = Slate700,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Year Adjuster Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200),
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = if (isUrdu) "$viewYear ھ" else "$viewYear AH",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate800,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
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
                                .testTag("hijri_next_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = if (isUrdu) "اگلا مہینہ" else "Next Islamic Month",
                                tint = Slate700,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Month Quick Picker Tabs (LazyRow)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val monthList = if (isUrdu) HijriDate.HIJRI_MONTHS_UR else HijriDate.HIJRI_MONTHS_EN
                itemsIndexed(monthList) { index, monthName ->
                    val monthIndex = index + 1
                    val isCurrentViewMonth = monthIndex == viewMonth
                    val isSacred = monthIndex == 1 || monthIndex == 7 || monthIndex == 11 || monthIndex == 12
                    val isRamadan = monthIndex == 9

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = when {
                            isCurrentViewMonth -> Emerald600
                            isRamadan -> Gold100
                            isSacred -> Emerald50
                            else -> Slate100
                        },
                        border = when {
                            isCurrentViewMonth -> null
                            isRamadan -> BorderStroke(1.dp, Gold400.copy(alpha = 0.5f))
                            isSacred -> BorderStroke(1.dp, Emerald100)
                            else -> BorderStroke(1.dp, Slate200)
                        },
                        shadowElevation = if (isCurrentViewMonth) 2.dp else 0.dp,
                        modifier = Modifier
                            .clickable {
                                viewMonth = monthIndex
                            }
                            .testTag("hijri_tab_month_$monthIndex")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            if (isRamadan) {
                                Text("✨ ", fontSize = 11.sp)
                            }
                            Text(
                                text = monthName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isCurrentViewMonth) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isCurrentViewMonth -> Color.White
                                    isRamadan -> Gold900
                                    isSacred -> Emerald800
                                    else -> Slate700
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Month Description Tag (e.g. Sacred Month / Ramadan)
            if (viewMonth == 9) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Gold100,
                    border = BorderStroke(1.dp, Gold400.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌙 ", fontSize = 14.sp)
                        Text(
                            text = if (isUrdu) {
                                "ماہِ صیام، نزولِ قرآن و لیلۃ القدر، رمضان المبارک"
                            } else {
                                "Holy Month of Ramadan (Fasting, Quran & Laylat al-Qadr)"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Gold900
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else if (viewMonth == 1 || viewMonth == 7 || viewMonth == 11 || viewMonth == 12) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald600.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐ ", fontSize = 12.sp)
                        Text(
                            text = if (isUrdu) {
                                "حرمت والے 4 مقدس مہینوں میں سے ایک (اشہر الحرم)"
                            } else {
                                "One of the 4 Sacred Months (Ashhur al-Hurum)"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Emerald800
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Hijri Days Grid
            val daysInMonth = HijriCalendarCalculator.getDaysInHijriMonth(viewYear, viewMonth)

            // Let's compute the weekday for Hijri day 1 to align columns:
            val day1Greg = HijriCalendarCalculator.toGregorian(viewYear, viewMonth, 1, moonOffset)
            val firstDayOfWeek = day1Greg.dayOfWeek // 1=Sun, 2=Mon ... 7=Sat
            val leadingEmptyDays = firstDayOfWeek - 1

            // Weekday Header Row
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
                                val isSelected = viewYear == selectedHijri.year &&
                                        viewMonth == selectedHijri.month &&
                                        dayNum == selectedHijri.day
                                val isToday = viewYear == todayHijri.year &&
                                        viewMonth == todayHijri.month &&
                                        dayNum == todayHijri.day
                                val isFriday = colIndex == 5
                                val isWhiteDay = dayNum in 13..15

                                // Moon phase icon
                                val moonPhase = HijriCalendarCalculator.getMoonPhaseInfo(dayNum)

                                // Corresponding Gregorian Day
                                val gregForDay = HijriCalendarCalculator.toGregorian(viewYear, viewMonth, dayNum, moonOffset)

                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            when {
                                                isSelected -> Emerald600
                                                isToday -> Emerald50
                                                isWhiteDay -> Gold100.copy(alpha = 0.6f)
                                                else -> Color.Transparent
                                            }
                                        )
                                        .then(
                                            when {
                                                isToday && !isSelected -> Modifier.border(1.5.dp, Emerald600, RoundedCornerShape(12.dp))
                                                isFriday && !isSelected -> Modifier.border(1.dp, Gold400.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                                isWhiteDay && !isSelected -> Modifier.border(1.dp, Gold400.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                                else -> Modifier
                                            }
                                        )
                                        .clickable {
                                            onHijriDateSelected(viewYear, viewMonth, dayNum)
                                        }
                                        .testTag("hijri_day_$dayNum"),
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
                                        // Corresponding Gregorian day number
                                        Text(
                                            text = "${gregForDay.day} ${if (isUrdu) gregForDay.monthNameUr else gregForDay.monthNameEn.take(3)}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
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

            // Hijri Quick Jump Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Today Hijri
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald50,
                    border = BorderStroke(1.dp, Emerald600.copy(alpha = 0.3f)),
                    modifier = Modifier.clickable {
                        viewYear = todayHijri.year
                        viewMonth = todayHijri.month
                        onHijriDateSelected(todayHijri.year, todayHijri.month, todayHijri.day)
                    }
                ) {
                    Text(
                        text = if (isUrdu) {
                            "آج (${todayHijri.day} ${todayHijri.monthNameUr})"
                        } else {
                            "Today (${todayHijri.day} ${todayHijri.monthNameEn.take(4)})"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Emerald800,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                }

                // 1st Ramadan
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Gold100,
                    border = BorderStroke(1.dp, Gold400.copy(alpha = 0.5f)),
                    modifier = Modifier.clickable {
                        viewYear = selectedHijri.year
                        viewMonth = 9
                        onHijriDateSelected(selectedHijri.year, 9, 1)
                    }
                ) {
                    Text(
                        text = if (isUrdu) "یکم رمضان المبارک" else "1st Ramadan",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Gold900,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                }

                // Eid al-Fitr (1 Shawwal)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate100,
                    border = BorderStroke(1.dp, Slate200),
                    modifier = Modifier.clickable {
                        viewYear = selectedHijri.year
                        viewMonth = 10
                        onHijriDateSelected(selectedHijri.year, 10, 1)
                    }
                ) {
                    Text(
                        text = if (isUrdu) "عید الفطر" else "Eid al-Fitr",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

private val Gold100 = Color(0xFFFEF3C7)
private val Gold300 = Color(0xFFFDE68A)
