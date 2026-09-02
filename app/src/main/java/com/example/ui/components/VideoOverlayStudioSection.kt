package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GregorianDate
import com.example.data.HijriDate
import com.example.data.IslamicOccasion
import com.example.data.OverlayTemplateStyle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald50
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Emerald700
import com.example.ui.theme.Emerald800
import com.example.ui.theme.Emerald900
import com.example.ui.theme.Gold300
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
import com.example.utils.OverlayFormat
import com.example.utils.OverlayImageGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun VideoOverlayStudioSection(
    hijriDate: HijriDate,
    gregorianDate: GregorianDate,
    occasion: IslamicOccasion?,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = true
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedStyle by remember { mutableStateOf(OverlayTemplateStyle.EMERALD_GOLD) }
    var selectedFormat by remember { mutableStateOf(OverlayFormat.STICKER_BADGE) }
    var customWatermark by remember { mutableStateOf("") }
    var showUrdu by remember(isUrdu) { mutableStateOf(isUrdu) }
    var isExporting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Title & Creator Note
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = if (isUrdu) "ویڈیو اوورلے" else "Video Overlay",
                    tint = Emerald700,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isUrdu) "ویڈیو ڈیٹ اسٹیکر و اوورلے اسٹوڈیو" else "Video Date Sticker & Overlay Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = if (isUrdu) {
                            "کیپ کٹ، ریلز، ٹک ٹاک اور واٹس ایپ اسٹیٹس کے لیے خوبصورت تاریخ اوورلے بنائیں۔"
                        } else {
                            "Generate stylish date overlays for CapCut, Reels, TikTok, and Status videos."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }
        }

        // Live Interactive Video Overlay Preview
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("video_overlay_preview_card"),
            shape = RoundedCornerShape(26.dp),
            color = Color.White.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, Slate200),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) "لائیو ویڈیو اوورلے پریویو" else "Live Video Overlay Preview",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Emerald700
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate100,
                        border = BorderStroke(1.dp, Slate200)
                    ) {
                        Text(
                            text = if (isUrdu) selectedFormat.labelUr else selectedFormat.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Slate700,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simulated Video Screen / Canvas Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(if (selectedFormat == OverlayFormat.STICKER_BADGE) 2.2f else if (selectedFormat == OverlayFormat.STORY_REEL_9_16) 0.75f else 1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF0F1715))
                        .border(1.dp, Gold400.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Background representation
                    when (selectedStyle) {
                        OverlayTemplateStyle.EMERALD_GOLD -> {
                            Image(
                                painter = painterResource(id = R.drawable.img_bg_emerald_gold),
                                contentDescription = "Emerald Gold Background",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Dimming layer
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0x77032D23),
                                                Color(0xCC021C16)
                                            )
                                        )
                                    )
                            )
                        }
                        OverlayTemplateStyle.NIGHT_CELESTIAL -> {
                            Image(
                                painter = painterResource(id = R.drawable.img_bg_night_mosque),
                                contentDescription = "Night Mosque Background",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0x550B132B),
                                                Color(0xBB030712)
                                            )
                                        )
                                    )
                            )
                        }
                        OverlayTemplateStyle.DESERT_SAND -> {
                            Image(
                                painter = painterResource(id = R.drawable.img_bg_aesthetic_sand),
                                contentDescription = "Desert Sand Background",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        OverlayTemplateStyle.TRANSPARENT_OVERLAY -> {
                            // Checkered video editor transparent simulation
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF1E2927))
                            )
                        }
                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF0D1F1A),
                                                Color(0xFF050F0D)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    // Floating Glass Card / Sticker inside the video frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                when (selectedStyle) {
                                    OverlayTemplateStyle.DESERT_SAND -> Color(0xE6FFFDF8)
                                    OverlayTemplateStyle.TRANSPARENT_OVERLAY -> Color(0xB30B1714)
                                    else -> Color(0xD9062019)
                                }
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFFC49A6C) else Gold400,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Watermark / Ayah Header
                            Text(
                                text = if (customWatermark.isNotBlank()) customWatermark else "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                fontWeight = FontWeight.Bold,
                                color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFFA67C1E) else Gold400
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Main Hijri Date
                            Text(
                                text = if (showUrdu) hijriDate.formattedUr else "${hijriDate.day} ${hijriDate.monthNameEn} ${hijriDate.year} AH",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                                fontWeight = FontWeight.Bold,
                                color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFF2C2416) else Color.White,
                                textAlign = TextAlign.Center
                            )

                            // Arabic Calligraphy Month
                            Text(
                                text = "${hijriDate.day} ${hijriDate.monthNameAr} ${hijriDate.year} هـ",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFF7A5816) else Gold300,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Gregorian Line
                            Text(
                                text = if (showUrdu) {
                                    "${gregorianDate.dayOfWeekNameUr} • ${gregorianDate.day} ${gregorianDate.monthNameUr} ${gregorianDate.year}ء"
                                } else {
                                    "${gregorianDate.dayOfWeekNameEn.uppercase()} • ${gregorianDate.day} ${gregorianDate.monthNameEn} ${gregorianDate.year}"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFF6B5B43) else Color(0xFFD1E7DD),
                                textAlign = TextAlign.Center
                            )

                            // Occasion Tag
                            val displayOccasion = occasion ?: if (gregorianDate.isFriday) {
                                IslamicOccasion("Jumu'ah Mubarak", "جمعة مباركة", "جمعۃ المبارک", "Jumu'ah", false)
                            } else null

                            if (displayOccasion != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "✨ ${if (showUrdu) displayOccasion.titleUr else displayOccasion.titleEn} ✨",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedStyle == OverlayTemplateStyle.DESERT_SAND) Color(0xFFA67C1E) else Gold400
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1. Choose Format (Sticker for Video vs Reel Story)
        Column {
            Text(
                text = if (isUrdu) "۱. اوورلے فارمیٹ منتخب کریں" else "1. Select Overlay Format",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OverlayFormat.values().forEach { format ->
                    val isSelected = selectedFormat == format
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) Emerald600 else Slate100,
                        border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedFormat = format }
                            .testTag("format_${format.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 11.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isUrdu) format.labelUr else format.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = if (isSelected) Color.White else Slate700
                            )
                        }
                    }
                }
            }
        }

        // 2. Choose Visual Style Theme
        Column {
            Text(
                text = if (isUrdu) "۲. اسٹائل منتخب کریں" else "2. Choose Visual Style",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(OverlayTemplateStyle.values()) { style ->
                    val isSelected = selectedStyle == style
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Emerald600 else Slate100,
                        border = if (isSelected) null else BorderStroke(1.dp, Slate200),
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .width(155.dp)
                            .clickable { selectedStyle = style }
                            .testTag("style_${style.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isUrdu) style.labelUr else style.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Slate900
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Gold300,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isUrdu) style.descriptionUr else style.description,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (isSelected) Color.White.copy(alpha = 0.85f) else Slate500
                            )
                        }
                    }
                }
            }
        }

        // 3. Custom Watermark / Title & Language Options
        Column {
            Text(
                text = if (isUrdu) "۳. اپنا عنوان یا واٹر مارک" else "3. Custom Header / Watermark",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = customWatermark,
                onValueChange = { customWatermark = it },
                label = {
                    Text(if (isUrdu) "چینل یا پیج کا نام (مثلاً: @mychannel یا اردو ولاگز)" else "Custom Title or Channel (e.g. @mychannel or Daily Vlog)")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("watermark_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Emerald600,
                    unfocusedBorderColor = Slate200,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Urdu / English Language switch chip
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Slate100,
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showUrdu = !showUrdu }
                    .testTag("lang_toggle_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = Emerald700,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) "اوورلے میں اردو و عربی تحریر دکھائیں" else "Display in Urdu & Arabic (اردو تاریخ موڈ)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Slate800
                        )
                    }
                    Text(
                        text = if (showUrdu) (if (isUrdu) "فعال (اردو)" else "ON (اردو)") else (if (isUrdu) "غیر فعال (English)" else "OFF (English)"),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (showUrdu) Emerald700 else Slate500
                    )
                }
            }
        }

        // Action Buttons: Save to Gallery & Share to Video Apps
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Save to Gallery
            Button(
                onClick = {
                    if (isExporting) return@Button
                    isExporting = true
                    coroutineScope.launch {
                        val bitmap = withContext(Dispatchers.Default) {
                            OverlayImageGenerator.renderOverlayBitmap(
                                context = context,
                                hijriDate = hijriDate,
                                gregorianDate = gregorianDate,
                                occasion = occasion,
                                style = selectedStyle,
                                format = selectedFormat,
                                customTitle = customWatermark,
                                showUrdu = showUrdu
                            )
                        }

                        val uri = withContext(Dispatchers.IO) {
                            OverlayImageGenerator.saveBitmapToGallery(context, bitmap, "Islamic_Video_Overlay")
                        }

                        isExporting = false
                        if (uri != null) {
                            Toast.makeText(
                                context,
                                if (isUrdu) "گیلری میں محفوظ ہو گیا! (Pictures/HijriDateStudio) 🎬" else "Saved to Gallery / Pictures/HijriDateStudio! 🎬",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                if (isUrdu) "تصویر محفوظ نہ ہو سکی" else "Failed to save image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("save_overlay_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald600,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = if (isUrdu) "گیلری میں محفوظ کریں" else "Save to Gallery",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isExporting) {
                        if (isUrdu) "محفوظ ہو رہا ہے..." else "Saving..."
                    } else {
                        if (isUrdu) "گیلری میں محفوظ کریں" else "Save to Gallery"
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            // Share directly to CapCut / Video Apps
            Button(
                onClick = {
                    coroutineScope.launch {
                        val bitmap = withContext(Dispatchers.Default) {
                            OverlayImageGenerator.renderOverlayBitmap(
                                context = context,
                                hijriDate = hijriDate,
                                gregorianDate = gregorianDate,
                                occasion = occasion,
                                style = selectedStyle,
                                format = selectedFormat,
                                customTitle = customWatermark,
                                showUrdu = showUrdu
                            )
                        }
                        OverlayImageGenerator.shareBitmap(
                            context = context,
                            bitmap = bitmap,
                            title = if (isUrdu) "اسلامی ویڈیو ڈیٹ اوورلے" else "Islamic Date Video Overlay"
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("share_overlay_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Slate900,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = if (isUrdu) "ایپس میں شیئر کریں" else "Share to Video Apps",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isUrdu) "ویڈیو ایپس میں شیئر" else "Share to Apps",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
