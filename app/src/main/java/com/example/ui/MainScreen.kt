package com.example.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GregorianDate
import com.example.data.HijriDate
import com.example.ui.components.ActiveDateHeroCard
import com.example.ui.components.GregorianDatePickerSection
import com.example.ui.components.HijriDatePickerSection
import com.example.ui.components.MoonSightingAdjustmentBar
import com.example.ui.components.VideoOverlayStudioSection
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.utils.HijriCalendarCalculator

enum class CalendarPickerMode {
    GREGORIAN,
    HIJRI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Language State: defaults to Urdu as requested
    var isUrdu by remember { mutableStateOf(true) }

    // Current Active Date State (Bidirectionally synced!)
    val todayGreg = remember { HijriCalendarCalculator.getTodayGregorian() }

    var moonOffset by remember { mutableIntStateOf(0) }
    var selectedGregorian by remember { mutableStateOf(todayGreg) }
    var selectedHijri by remember(selectedGregorian, moonOffset) {
        mutableStateOf(
            HijriCalendarCalculator.toHijri(
                selectedGregorian.year,
                selectedGregorian.month,
                selectedGregorian.day,
                moonOffset
            )
        )
    }

    // Active Tab: 0 = Dual Calendar Picker, 1 = Video Overlay Studio
    var activeTab by remember { mutableIntStateOf(0) }

    // Inside Calendar Tab: Gregorian vs Hijri Picker toggle
    var calendarPickerMode by remember { mutableStateOf(CalendarPickerMode.GREGORIAN) }

    // Calculated occasion
    val occasion = remember(selectedHijri, selectedGregorian) {
        HijriCalendarCalculator.getIslamicOccasion(selectedHijri, selectedGregorian)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Emerald600,
                            shadowElevation = 3.dp,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NightsStay,
                                contentDescription = if (isUrdu) "ہلال" else "Islamic Crescent",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(7.dp)
                                    .size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isUrdu) "ہجری و شمسی کیلنڈر" else "Hijri Date Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = if (isUrdu) "اسلامی تقویم و ویڈیو اوورلے" else "Islamic & Gregorian Calendar",
                                style = MaterialTheme.typography.labelSmall,
                                color = Slate500
                            )
                        }
                    }
                },
                actions = {
                    // Language Switcher Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isUrdu) Emerald50 else Slate100,
                        border = BorderStroke(1.dp, if (isUrdu) Emerald600.copy(alpha = 0.4f) else Slate200),
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        onClick = { isUrdu = !isUrdu }
                    ) {
                        Text(
                            text = if (isUrdu) "اردو" else "English",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isUrdu) Emerald800 else Slate700,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // Reset to Today Action with Frosted Circular Button
                    Surface(
                        shape = CircleShape,
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                selectedGregorian = todayGreg
                                selectedHijri = HijriCalendarCalculator.toHijri(
                                    todayGreg.year,
                                    todayGreg.month,
                                    todayGreg.day,
                                    moonOffset
                                )
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("appbar_today_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Today,
                                contentDescription = if (isUrdu) "آج کی تاریخ" else "Reset to Today",
                                tint = Emerald700,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f),
                    titleContentColor = Slate900
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate50)
        ) {
            // Frosted Glass Main Navigation Segmented Control
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Slate100,
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    // Dual Calendar Tab Pill
                    val isCalActive = activeTab == 0
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isCalActive) Emerald600 else Color.Transparent,
                        shadowElevation = if (isCalActive) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("tab_calendar"),
                        onClick = { activeTab = 0 }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = if (isCalActive) Color.White else Slate600,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "دوہری تقویم" else "Dual Calendar",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isCalActive) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCalActive) Color.White else Slate600
                            )
                        }
                    }

                    // Video Overlay Tab Pill
                    val isVideoActive = activeTab == 1
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isVideoActive) Emerald600 else Color.Transparent,
                        shadowElevation = if (isVideoActive) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .testTag("tab_video_overlay"),
                        onClick = { activeTab = 1 }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MovieCreation,
                                contentDescription = null,
                                tint = if (isVideoActive) Color.White else Slate600,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "ویڈیو اوورلے" else "Video Overlay",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isVideoActive) FontWeight.Bold else FontWeight.Medium,
                                color = if (isVideoActive) Color.White else Slate600
                            )
                        }
                    }
                }
            }

            // Tab Content
            if (activeTab == 0) {
                // Calendar Screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Active Selected Date Hero Card
                    ActiveDateHeroCard(
                        hijriDate = selectedHijri,
                        gregorianDate = selectedGregorian,
                        occasion = occasion,
                        onGoToVideoOverlay = { activeTab = 1 },
                        isUrdu = isUrdu
                    )

                    // Moon Sighting Calibration Bar
                    MoonSightingAdjustmentBar(
                        currentOffset = moonOffset,
                        onOffsetChanged = { offset ->
                            moonOffset = offset
                            selectedHijri = HijriCalendarCalculator.toHijri(
                                selectedGregorian.year,
                                selectedGregorian.month,
                                selectedGregorian.day,
                                offset
                            )
                        },
                        isUrdu = isUrdu
                    )

                    // Calendar Mode Switcher Pill (Gregorian vs Hijri Calendar)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            // Gregorian Tab Pill
                            val isGreg = calendarPickerMode == CalendarPickerMode.GREGORIAN
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isGreg) Emerald600 else Color.Transparent,
                                shadowElevation = if (isGreg) 2.dp else 0.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .testTag("switch_gregorian_picker"),
                                onClick = { calendarPickerMode = CalendarPickerMode.GREGORIAN }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = if (isGreg) Color.White else Slate700,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isUrdu) "شمسی تقویم (عیسوی)" else "Gregorian Picker (شمسی)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isGreg) Color.White else Slate700
                                    )
                                }
                            }

                            // Hijri Tab Pill
                            val isHijri = calendarPickerMode == CalendarPickerMode.HIJRI
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isHijri) Emerald600 else Color.Transparent,
                                shadowElevation = if (isHijri) 2.dp else 0.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .testTag("switch_hijri_picker"),
                                onClick = { calendarPickerMode = CalendarPickerMode.HIJRI }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NightsStay,
                                        contentDescription = null,
                                        tint = if (isHijri) Color.White else Slate700,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isUrdu) "قمری تقویم (ہجری)" else "Hijri Picker (قمری)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isHijri) Color.White else Slate700
                                    )
                                }
                            }
                        }
                    }

                    // Display Selected Calendar Picker View
                    if (calendarPickerMode == CalendarPickerMode.GREGORIAN) {
                        GregorianDatePickerSection(
                            selectedDate = selectedGregorian,
                            onDateSelected = { year, month, day ->
                                val jd = HijriCalendarCalculator.gregorianToJulianDay(year, month, day)
                                val greg = HijriCalendarCalculator.julianDayToGregorian(jd)
                                selectedGregorian = greg
                                selectedHijri = HijriCalendarCalculator.toHijri(year, month, day, moonOffset)
                            },
                            moonOffset = moonOffset,
                            isUrdu = isUrdu
                        )
                    } else {
                        HijriDatePickerSection(
                            selectedHijri = selectedHijri,
                            onHijriDateSelected = { year, month, day ->
                                val hijri = HijriDate(day, month, year)
                                selectedHijri = hijri
                                selectedGregorian = HijriCalendarCalculator.toGregorian(year, month, day, moonOffset)
                            },
                            moonOffset = moonOffset,
                            isUrdu = isUrdu
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // Video Overlay Studio Screen
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    VideoOverlayStudioSection(
                        hijriDate = selectedHijri,
                        gregorianDate = selectedGregorian,
                        occasion = occasion,
                        isUrdu = isUrdu
                    )
                }
            }
        }
    }
}
