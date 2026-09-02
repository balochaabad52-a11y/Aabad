package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GregorianDate
import com.example.data.HijriDate
import com.example.data.IslamicOccasion
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.Gold300
import com.example.ui.theme.Gold400
import com.example.ui.theme.Gold500
import com.example.ui.theme.Teal700
import com.example.ui.theme.Teal800
import com.example.utils.HijriCalendarCalculator

@Composable
fun ActiveDateHeroCard(
    hijriDate: HijriDate,
    gregorianDate: GregorianDate,
    occasion: IslamicOccasion?,
    onGoToVideoOverlay: () -> Unit,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = true
) {
    val context = LocalContext.current
    val moonInfo = HijriCalendarCalculator.getMoonPhaseInfo(hijriDate.day)
    val moonInfoUrdu = HijriCalendarCalculator.getMoonPhaseInfoUrdu(hijriDate.day)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Emerald900,
                            Teal800,
                            Emerald700
                        )
                    )
                )
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Tag: Frosted Moon Phase & Copy Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Moon Phase Frosted Glass Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isUrdu) moonInfoUrdu.second else moonInfo.second, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "${moonInfoUrdu.first} (چاند کا ${hijriDate.day} واں دن)" else "Day ${hijriDate.day} Moon (${moonInfo.first})",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Copy Date Text Frosted Glass Button
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                    ) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipText = if (isUrdu) {
                                    "${hijriDate.formattedUr} (${hijriDate.formattedAr})\n${gregorianDate.formattedFullUr}"
                                } else {
                                    "${hijriDate.formattedEn} (${hijriDate.formattedAr})\n${gregorianDate.formattedFull}"
                                }
                                val clip = ClipData.newPlainText("Islamic Date", clipText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(
                                    context,
                                    if (isUrdu) "تاریخ کاپی ہو گئی! ✨" else "Date copied to clipboard! ✨",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("copy_hero_date_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = if (isUrdu) "تاریخ کاپی کریں" else "Copy Formatted Date",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Arabic Bismillah / Heading
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    style = MaterialTheme.typography.titleMedium,
                    color = Gold300,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Primary Hijri Date Display
                Text(
                    text = if (isUrdu) "${hijriDate.day} ${hijriDate.monthNameUr} ${hijriDate.year} ھ" else "${hijriDate.day} ${hijriDate.monthNameEn} ${hijriDate.year} AH",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 27.sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Arabic & Secondary Month Script
                Text(
                    text = if (isUrdu) {
                        "${hijriDate.day} ${hijriDate.monthNameAr} ${hijriDate.year} هـ  •  ${hijriDate.day} ${hijriDate.monthNameEn} ${hijriDate.year} AH"
                    } else {
                        "${hijriDate.day} ${hijriDate.monthNameAr} ${hijriDate.year} هـ  •  ${hijriDate.formattedUr}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Frosted Decorative Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Gregorian Date
                Text(
                    text = if (isUrdu) {
                        "${gregorianDate.dayOfWeekNameUr}  •  ${gregorianDate.day} ${gregorianDate.monthNameUr} ${gregorianDate.year}ء"
                    } else {
                        "${gregorianDate.dayOfWeekNameEn.uppercase()}  •  ${gregorianDate.day} ${gregorianDate.monthNameEn} ${gregorianDate.year}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )

                // Occasion Badge if any
                val displayOccasion = occasion ?: if (gregorianDate.isFriday) {
                    IslamicOccasion("Jumu'ah Mubarak", "جمعة مباركة", "جمعۃ المبارک", "Jumu'ah", false)
                } else null

                if (displayOccasion != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.16f),
                        border = BorderStroke(1.dp, Gold400.copy(alpha = 0.8f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✨", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isUrdu) "${displayOccasion.titleUr}  (${displayOccasion.titleAr})" else "${displayOccasion.titleEn}  (${displayOccasion.titleUr})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Gold300
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action: Frosted Pill Button for Video Overlay Studio
                Button(
                    onClick = onGoToVideoOverlay,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Emerald900
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("create_video_overlay_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.MovieCreation,
                        contentDescription = if (isUrdu) "ویڈیو اوورلے" else "Video Overlay",
                        tint = Emerald800,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isUrdu) "ویڈیو ڈیٹ اسٹیکر / اوورلے بنائیں" else "Create Video Date Sticker / Overlay",
                        fontWeight = FontWeight.Bold,
                        color = Emerald900,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
